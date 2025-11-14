package com.app.hungrify.main.service;


import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.menu.*;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.*;
import com.app.hungrify.main.repository.FoodItemIngredientRepository;
import com.app.hungrify.main.repository.FoodItemProfileRepository;
import com.app.hungrify.main.repository.FoodItemRepository;
import com.app.hungrify.main.repository.IngredientRepository;
import com.app.hungrify.main.repository.RestaurantRepository;
import com.app.hungrify.main.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;
import java.time.Instant;
import java.math.BigDecimal;

/**
 * Implementation that uses repositories only.
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final CommonUtils commonUtils;
    private final UserRepository userRepository;
    private final com.app.hungrify.main.repository.RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;
    private final FoodItemProfileRepository profileRepository;
    private final FoodItemIngredientRepository fiRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public GroupedMenuResponseDto getGroupedMenu(Long restaurantId) {
        List<FoodItem> items = foodItemRepository.findAllByRestaurantWithProfile(restaurantId);
        // group by categoryName (profile)
//        Map<String, List<FoodItemSummaryDto>> groups = items.stream().map(this::toSummary).collect(Collectors.groupingBy(f -> f.getCategoryName() == null ? "Uncategorized" : f.getCategoryName()));

        Map<String, List<FoodItemSummaryDto>> groups = items.stream()
                .collect(Collectors.groupingBy(
                        i -> (i.getProfile() == null || i.getProfile().getCategoryName() == null)
                                ? "Uncategorized"
                                : i.getProfile().getCategoryName(),
                        Collectors.mapping(this::toSummary, Collectors.toList())
                ));
        List<MenuGroupDto> groupDtos = groups.entrySet().stream()
                .map(e -> MenuGroupDto.builder().categoryName(e.getKey()).categoryCode(e.getKey().toLowerCase().replace(" ", "_")).items(e.getValue()).build())
                .collect(Collectors.toList());
        return GroupedMenuResponseDto.builder().restaurantId(restaurantId).groups(groupDtos).build();
    }

    @Override
    public FoodItemDetailDto getItemDetail(Long itemId) {
        Restaurant restaurant = getRestaurantByUserId();
        Long restaurantIdResolved = restaurant.getRestaurantId();
        FoodItem item = foodItemRepository.findByItemIdAndRestaurant_RestaurantId(itemId, restaurantIdResolved)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        FoodItemDetailDto dto = toDetail(item);
        // ingredients
        List<FoodItemIngredient> fis = fiRepository.findByFoodItem_ItemId(itemId);
        List<IngredientSimpleDto> ingr = fis.stream().map(fi -> {
            Ingredient ing = fi.getIngredient();
            return IngredientSimpleDto.builder().ingredientId(ing.getIngredientId()).name(ing.getName()).displayName(ing.getDisplayName()).removable(fi.getRemovable()).allergens(ing.getAllergens()).build();
        }).collect(Collectors.toList());
        dto.setIngredients(ingr);
        return dto;
    }

    @Override
    @Transactional
    public FoodItemDetailDto createItem(CreateFoodItemRequestDto request) {
        // validation: canonical uniqueness
        Restaurant restaurant = getRestaurantByUserId();
        Long restaurantIdResolved = restaurant.getRestaurantId();
        if (foodItemRepository.existsByCanonicalNameAndRestaurant_RestaurantId(request.getCanonicalName(), restaurantIdResolved)) {
            throw new BadRequestException("canonicalName already exists for this restaurant");
        }
        FoodItem item = new FoodItem();
        item.setRestaurant(restaurant);
        item.setCanonicalName(request.getCanonicalName());
        item.setDisplayName(request.getDisplayName());
        item.setShortDescription(request.getShortDescription());
        item.setLongDescription(request.getLongDescription());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());
        item.setIsAvailable(request.getIsAvailable() == null ? true : request.getIsAvailable());
        item.setPrepTimeMinutes(request.getPrepTimeMinutes());
        item.setImageUrls(request.getImageUrls());
        FoodItem saved = foodItemRepository.save(item);

        // profile
        if (request.getProfile() != null) {
            FoodItemProfileDto p = request.getProfile();
            FoodItemProfile profile = new FoodItemProfile();
            profile.setItem(saved);
            profile.setCategoryCode(p.getCategoryCode());
            profile.setCategoryName(p.getCategoryName());
            profile.setCaloriesKcal(p.getCaloriesKcal());
            profile.setCarbsG(p.getCarbsG());
            profile.setProteinG(p.getProteinG());
            profile.setFatsG(p.getFatsG());
            profile.setSpiceScore(p.getSpiceScore());
            profile.setSpiceLevel(p.getSpiceLevel() == null ? null : FoodItemProfile.SpiceLevel.valueOf(p.getSpiceLevel().toLowerCase()));
            profile.setAllergens(p.getAllergens());
            profile.setTags(p.getTags());
            profile.setTasteProfile(p.getTasteProfile());
            profileRepository.save(profile);
        }

        // ingredients - link existing ingredient rows or create lightweight ones
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            for (CreateIngredientDto ci : request.getIngredients()) {
                Ingredient ingredient = null;
                if (ci.getIngredientId() != null) {
                    ingredient = ingredientRepository.findById(ci.getIngredientId()).orElse(null);
                }
                if (ingredient == null) {
                    ingredient = ingredientRepository.findByNameIgnoreCase(ci.getName()).orElse(null);
                }
                if (ingredient == null) {
                    ingredient = new Ingredient();
                    ingredient.setName(ci.getName());
                    ingredient.setDisplayName(ci.getName());
                    ingredient.setAllergens(ci.getAllergens());
                    ingredientRepository.save(ingredient);
                }
                FoodItemIngredient fii = new FoodItemIngredient();
                fii.setFoodItem(saved);
                fii.setIngredient(ingredient);
                fii.setRemovable(ci.getRemovable() == null ? false : ci.getRemovable());
                fiRepository.save(fii);
            }
        }

        return getItemDetail(saved.getItemId());
    }

    @Override
    @Transactional
    public FoodItemDetailDto updateItem(Long itemId, UpdateFoodItemRequestDto request) {
        Restaurant restaurant = getRestaurantByUserId();
        Long restaurantIdResolved = restaurant.getRestaurantId();
        FoodItem item = foodItemRepository.findByItemIdAndRestaurant_RestaurantId(itemId, restaurantIdResolved).orElseThrow(() -> new NotFoundException("Item not found"));
        if (request.getCanonicalName() != null) item.setCanonicalName(request.getCanonicalName());
        if (request.getDisplayName() != null) item.setDisplayName(request.getDisplayName());
        if (request.getShortDescription() != null) item.setShortDescription(request.getShortDescription());
        if (request.getLongDescription() != null) item.setLongDescription(request.getLongDescription());
        if (request.getPrice() != null) item.setPrice(request.getPrice());
        if (request.getQuantity() != null) item.setQuantity(request.getQuantity());
        if (request.getIsAvailable() != null) item.setIsAvailable(request.getIsAvailable());
        if (request.getPrepTimeMinutes() != null) item.setPrepTimeMinutes(request.getPrepTimeMinutes());
        if (request.getImageUrls() != null) item.setImageUrls(request.getImageUrls());
        FoodItem saved = foodItemRepository.save(item);

        // profile update
        if (request.getProfile() != null) {
            FoodItemProfileDto p = request.getProfile();
            FoodItemProfile profile = profileRepository.findByItem_ItemId(itemId).orElseGet(() -> {
                FoodItemProfile np = new FoodItemProfile();
                np.setItem(saved);
                return np;
            });
            profile.setCategoryCode(p.getCategoryCode());
            profile.setCategoryName(p.getCategoryName());
            profile.setCaloriesKcal(p.getCaloriesKcal());
            profile.setCarbsG(p.getCarbsG());
            profile.setProteinG(p.getProteinG());
            profile.setFatsG(p.getFatsG());
            profile.setSpiceScore(p.getSpiceScore());
            if (p.getSpiceLevel() != null) profile.setSpiceLevel(FoodItemProfile.SpiceLevel.valueOf(p.getSpiceLevel().toLowerCase()));
            profile.setAllergens(p.getAllergens());
            profile.setTags(p.getTags());
            profile.setTasteProfile(p.getTasteProfile());
            profileRepository.save(profile);
        }

        // ingredients replacement - simple approach: delete existing and re-add
        if (request.getIngredients() != null) {
            fiRepository.deleteByFoodItem_ItemId(itemId);
            for (CreateIngredientDto ci : request.getIngredients()) {
                Ingredient ingredient = null;
                if (ci.getIngredientId() != null) {
                    ingredient = ingredientRepository.findById(ci.getIngredientId()).orElse(null);
                }
                if (ingredient == null) {
                    ingredient = ingredientRepository.findByNameIgnoreCase(ci.getName()).orElse(null);
                }
                if (ingredient == null) {
                    ingredient = new Ingredient();
                    ingredient.setName(ci.getName());
                    ingredient.setDisplayName(ci.getName());
                    ingredient.setAllergens(ci.getAllergens());
                    ingredientRepository.save(ingredient);
                }
                FoodItemIngredient fii = new FoodItemIngredient();
                fii.setFoodItem(saved);
                fii.setIngredient(ingredient);
                fii.setRemovable(ci.getRemovable() == null ? false : ci.getRemovable());
                fiRepository.save(fii);
            }
        }

        return getItemDetail(itemId);
    }

    @Override
    @Transactional
    public FoodItemDetailDto patchItem(Long itemId, PatchFoodItemRequestDto request) {
        Restaurant restaurant = getRestaurantByUserId();
        Long restaurantIdResolved = restaurant.getRestaurantId();
        FoodItem item = foodItemRepository.findByItemIdAndRestaurant_RestaurantId(itemId, restaurantIdResolved).orElseThrow(() -> new NotFoundException("Item not found"));
        if (request.getIsAvailable() != null) item.setIsAvailable(request.getIsAvailable());
        if (request.getQuantity() != null) item.setQuantity(request.getQuantity());
        if (request.getPrice() != null) item.setPrice(request.getPrice());
        foodItemRepository.save(item);
        return getItemDetail(itemId);
    }

    @Override
    @Transactional
    public void softDeleteItem(Long itemId) {
        Restaurant restaurant = getRestaurantByUserId();
        Long restaurantIdResolved = restaurant.getRestaurantId();
        FoodItem item = foodItemRepository.findByItemIdAndRestaurant_RestaurantId(itemId, restaurantIdResolved).orElseThrow(() -> new NotFoundException("Item not found"));
        item.setIsAvailable(false);
        foodItemRepository.save(item);
    }

    @Override
    public List<LowStockDto> getLowStock(Long restaurantId) {
        Restaurant restaurant = getRestaurantByUserId();
        Long restaurantIdResolved = restaurant.getRestaurantId();
        List<FoodItem> available = foodItemRepository.findAvailableByRestaurant(restaurantIdResolved);
        List<LowStockDto> result = new ArrayList<>();
        for (FoodItem f : available) {
            int q = f.getQuantity() == null ? 0 : f.getQuantity();
            // threshold from profile if present else default
            int threshold = 5;
            FoodItemProfile profile = profileRepository.findByItem_ItemId(f.getItemId()).orElse(null);
            if (profile != null && profile.getTags() != null) {
                // no threshold field in profile - keep default
            }
            if (q <= threshold) {
                result.add(LowStockDto.builder().itemId(f.getItemId()).displayName(f.getDisplayName()).quantity(q).threshold(threshold).isTracked(true).build());
            }
        }
        return result;
    }

    @Override
    public ParseIngredientsResponseDto parseIngredients(ParseIngredientsRequestDto request) {
        // Mocked parser: split commas, normalize (trim/lowercase), attempt to match existing Ingredient rows.
        String input = request.getText();
        String[] parts = input.split(",");
        List<SuggestedIngredientDto> suggested = new ArrayList<>();
        Set<String> allergens = new HashSet<>();
        for (String raw : parts) {
            String n = raw.trim().toLowerCase();
            if (n.isEmpty()) continue;
            Optional<Ingredient> existing = ingredientRepository.findByNameIgnoreCase(n);
            if (existing.isPresent()) {
                Ingredient ing = existing.get();
                suggested.add(SuggestedIngredientDto.builder().ingredientId(ing.getIngredientId()).name(ing.getName()).displayName(ing.getDisplayName()).allergens(ing.getAllergens()).build());
                if (ing.getAllergens() != null) allergens.addAll(ing.getAllergens());
            } else {
                // suggested new ingredient
                List<String> empty = Collections.emptyList();
                suggested.add(SuggestedIngredientDto.builder().ingredientId(null).name(n).displayName(capitalize(n)).allergens(empty).build());
            }
        }
        String notes = "This is a mocked parser. Review suggested ingredients and allergens.";
        return ParseIngredientsResponseDto.builder().input(input).suggestedIngredients(suggested).allergens(new ArrayList<>(allergens)).notes(notes).build();
    }

    // helpers
    private FoodItemSummaryDto toSummary(FoodItem f) {
        FoodItemSummaryDto dto = FoodItemSummaryDto.builder()
                .itemId(f.getItemId())
                .canonicalName(f.getCanonicalName())
                .displayName(f.getDisplayName())
                .shortDescription(f.getShortDescription())
                .price(f.getPrice())
                .quantity(f.getQuantity())
                .isAvailable(f.getIsAvailable())
                .prepTimeMinutes(f.getPrepTimeMinutes())
                .imageUrls(f.getImageUrls())
                .rating(f.getRating())
                .lastUpdated(f.getUpdatedAt())
                .build();
        // categoryName set later in grouping from profile join query
        if (f.getProfile() != null) {
            dto.setShortDescription(dto.getShortDescription() == null ? f.getProfile().getCategoryName() : dto.getShortDescription());
        }
        return dto;
    }

    private FoodItemDetailDto toDetail(FoodItem f) {
        FoodItemDetailDto d = new FoodItemDetailDto();
        d.setItemId(f.getItemId());
        d.setRestaurantId(f.getRestaurant() == null ? null : f.getRestaurant().getRestaurantId());
        d.setCanonicalName(f.getCanonicalName());
        d.setDisplayName(f.getDisplayName());
        d.setShortDescription(f.getShortDescription());
        d.setLongDescription(f.getLongDescription());
        d.setPrice(f.getPrice());
        d.setQuantity(f.getQuantity());
        d.setIsAvailable(f.getIsAvailable());
        d.setPrepTimeMinutes(f.getPrepTimeMinutes());
        d.setImageUrls(f.getImageUrls());
        d.setRating(f.getRating());
        d.setCreatedAt(f.getCreatedAt());
        d.setUpdatedAt(f.getUpdatedAt());
        FoodItemProfile p = f.getProfile();
        if (p != null) {
            d.setCategoryCode(p.getCategoryCode());
            d.setCategoryName(p.getCategoryName());
            d.setCaloriesKcal(p.getCaloriesKcal());
            d.setCarbsG(p.getCarbsG());
            d.setProteinG(p.getProteinG());
            d.setFatsG(p.getFatsG());
            d.setSpiceScore(p.getSpiceScore());
            d.setSpiceLevel(p.getSpiceLevel() == null ? null : p.getSpiceLevel().name());
            d.setAllergens(p.getAllergens());
            d.setTags(p.getTags());
            d.setTasteProfile(p.getTasteProfile());
        }
        return d;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private Restaurant getRestaurantByUserId() {
        Long userId = commonUtils.getUserId();
        return restaurantRepository.findByOwner_UserId(userId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found for user: " + userId));
    }
}
