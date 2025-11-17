package com.app.hungrify.main.template;

import com.app.hungrify.main.dto.menu.CreateFoodItemRequestDto;
import com.app.hungrify.main.dto.menu.CreateIngredientDto;
import com.app.hungrify.main.dto.search.AiSearch;
import com.app.hungrify.main.dto.search.SearchFoodItemDto;
import com.app.hungrify.main.dto.search.SearchResponseDto;
import com.app.hungrify.main.dto.search.SearchRestaurantDto;
import com.app.hungrify.main.models.FoodItem;
import com.app.hungrify.main.repository.FoodItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromptTemplate {


    private final ChatService chatService;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final FoodItemRepository foodItemRepository;


    public List<Map<String, Object>> executeAiQuery(String sqlQuery) {
        return jdbcTemplate.queryForList(sqlQuery);
    }

    public static String extractJson(String input) {
        int start = input.indexOf('{');
        int end = input.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return input.substring(start, end + 1);
        }
        return null; // or throw exception if JSON not found
    }

    public SearchResponseDto searchFood(String query) throws IOException {
        String search = Files.readString(Paths.get("src/main/resources/search-query"));
        String prompt = search.replace("<<<INPUT_SEARCH_QUERY>>>", query);

        String response = chatService.sendPrompt(prompt);
        response = extractJson(response);
        AiSearch aiSearch = objectMapper.readValue(response, AiSearch.class);
        log.info("AI Search SQL Query: {}", aiSearch.getSqlQuery());

        List<Map<String, Object>> results = executeAiQuery(aiSearch.getSqlQuery());
        log.info("AI Search Results: {}", results);

        List<SearchRestaurantDto> restaurantDtos = new ArrayList<>();
        int totalFoodMatches = 0;
        int totalRestaurants = 0;
        String city = null;

        if (aiSearch.getType().equalsIgnoreCase("food")) {
            // Group results by restaurantId
            Map<Long, List<Map<String, Object>>> grouped = new java.util.HashMap<>();
            for (Map<String, Object> row : results) {
                Long restaurantId = ((Number) row.get("restaurant_id")).longValue();
                grouped.computeIfAbsent(restaurantId, k -> new ArrayList<>()).add(row);
            }
            totalRestaurants = grouped.size();
            totalFoodMatches = results.size();

            for (Map.Entry<Long, List<Map<String, Object>>> entry : grouped.entrySet()) {
                Long restaurantId = entry.getKey();
                // Fetch restaurant details
                Map<String, Object> restDetails = executeAiQuery(
                        "SELECT * FROM restaurants WHERE restaurant_id = " + restaurantId
                ).get(0);

                city = (String) restDetails.get("city");

                List<SearchFoodItemDto> foods = new ArrayList<>();
                for (Map<String, Object> foodRow : entry.getValue()) {
                    FoodItem foodItem = foodItemRepository.findById(((Number) foodRow.get("item_id")).longValue()).orElse(null);
                    if (foodItem != null) {
                        foods.add(SearchFoodItemDto.builder()
                                .itemId(foodItem.getItemId())
                                .displayName(foodItem.getDisplayName())
                                .shortDescription(foodItem.getShortDescription())
                                .price(foodItem.getPrice())
                                .imageUrls(foodItem.getImageUrls())
                                .rating(foodItem.getRating())
//                                .categoryName(foodItem.ge)
                                .build());
                    }

                }

                restaurantDtos.add(SearchRestaurantDto.builder()
                        .restaurantId(restaurantId)
                        .name((String) restDetails.get("name"))
                        .cuisine((String) restDetails.get("cuisine"))
                        .address((String) restDetails.get("address"))
                        .city((String) restDetails.get("city"))
                        .isActive(restDetails.get("is_active") != null ? (Boolean) restDetails.get("is_active") : null)
                        .latitude(restDetails.get("latitude") != null ? new BigDecimal(restDetails.get("latitude").toString()) : null)
                        .longitude(restDetails.get("longitude") != null ? new BigDecimal(restDetails.get("longitude").toString()) : null)
                        .foods(foods)
                        .build());
            }
        } else {
            // Restaurant search
            totalRestaurants = results.size();
            for (Map<String, Object> restRow : results) {
                Long restaurantId = ((Number) restRow.get("restaurant_id")).longValue();
                city = (String) restRow.get("city");
                restaurantDtos.add(SearchRestaurantDto.builder()
                        .restaurantId(restaurantId)
                        .name((String) restRow.get("name"))
                        .cuisine((String) restRow.get("cuisine"))
                        .address((String) restRow.get("address"))
                        .city((String) restRow.get("city"))
                        .isActive(restRow.get("is_active") != null ? (Boolean) restRow.get("is_active") : null)
                        .latitude(restRow.get("latitude") != null ? new BigDecimal(restRow.get("latitude").toString()) : null)
                        .longitude(restRow.get("longitude") != null ? new BigDecimal(restRow.get("longitude").toString()) : null)
                        .foods(new ArrayList<>())
                        .build());
            }
        }

        return SearchResponseDto.builder()
                .query(query)
                .city(city)
                .totalRestaurants(totalRestaurants)
                .totalFoodMatches(totalFoodMatches)
                .restaurants(restaurantDtos)
                .build();
    }


    public CreateFoodItemRequestDto addFood(String itemName, String itemShortDescription, String categoryName, List<String> presentIngredients) throws IOException {
        String ingredients = Files.readString(Paths.get("src/main/resources/ingredients"));
        String parseIngredients = Files.readString(Paths.get("src/main/resources/parse-ingredients"));
        String ingredientCsv = String.join(",", presentIngredients);
        String ingredientPrompt = ingredients
                .replace("<<<INPUT_ITEM_NAME>>>", itemName)
                .replace("<<<INPUT_SHORT_DESC>>>", itemShortDescription)
                .replace("<<<INPUT_CATEGORY_NAME>>>", categoryName);

        String ingredientsReponse = chatService.sendPrompt(ingredientPrompt);

        List<CreateIngredientDto> createIngredient = CreateIngredientDto.listFromJson(ingredientsReponse);

        List<String> predictedIngredients = createIngredient.stream().map(CreateIngredientDto::getName).toList();

        log.info("Original ingredients: {}", ingredientCsv);
        log.info("Predicted ingredients: {}", predictedIngredients);
        List<String> finalIngredients = getFinalMatchedIngredients(presentIngredients, predictedIngredients, 0.85);
        log.info("Final ingredients: {}", finalIngredients);
        String finalIngredientsCsv = String.join(",", finalIngredients);

        String prompt = parseIngredients
                .replace("<<<INPUT_ITEM_NAME>>>", itemName)
                .replace("<<<INPUT_SHORT_DESC>>>", itemShortDescription)
                .replace("<<<INPUT_CATEGORY_NAME>>>", categoryName)
                .replace("<<<INPUT_INGREDIENT_CSV>>>", finalIngredientsCsv);

        String response = chatService.sendPrompt(prompt);
        return CreateFoodItemRequestDto.fromJson(response);
    }


    public static List<String> getFinalMatchedIngredients(List<String> original, List<String> predicted, double threshold) {
        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
        List<String> finalList = new ArrayList<>();
        for (String pred : predicted) {
            String bestMatch = pred;
            double bestScore = threshold;
            for (String orig : original) {
                double score = similarity.apply(orig, pred);
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = orig;
                }
            }
            finalList.add(bestMatch);
        }
        return finalList;
    }
}
