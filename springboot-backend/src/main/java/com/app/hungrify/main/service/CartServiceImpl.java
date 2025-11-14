package com.app.hungrify.main.service;

import com.app.hungrify.common.models.Users;
import com.app.hungrify.common.repository.UserRepository;
import com.app.hungrify.main.dto.cart.*;
import com.app.hungrify.main.exception.BadRequestException;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.util.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Handles CRUD operations on user's cart inside profile_json.
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommonUtils commonUtils;

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getCart() {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = user.getProfileJson();
        if (profile == null || !profile.containsKey("cart")) {
            return emptyCart();
        }

        return objectMapper.convertValue(profile.get("cart"), CartResponseDto.class);
    }

    @Override
    @Transactional
    public CartResponseDto addToCart( CartAddRequestDto request) {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = user.getProfileJson() != null ? user.getProfileJson() : new HashMap<>();
        CartResponseDto cart = profile.containsKey("cart")
                ? objectMapper.convertValue(profile.get("cart"), CartResponseDto.class)
                : emptyCart();

        // Single restaurant constraint
        if (cart.getRestaurantId() != null && !cart.getRestaurantId().equals(request.getRestaurantId())) {
//            throw new BadRequestException("Cart already contains items from another restaurant");
            clearCart();
        }

        cart.setRestaurantId(request.getRestaurantId());
        List<CartItemDto> items = cart.getItems() != null ? new ArrayList<>(cart.getItems()) : new ArrayList<>();
        Optional<CartItemDto> existing = items.stream()
                .filter(i -> i.getItemId().equals(request.getItemId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItemDto item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            items.add(CartItemDto.builder()
                    .itemId(request.getItemId())
                    .displayName(request.getDisplayName())
                    .quantity(request.getQuantity())
                    .unitPrice(request.getUnitPrice())
                    .imageUrl(request.getImageUrl())
                    .build());
        }

        cart.setItems(items);
        updateTotals(cart);

        profile.put("cart", cart);
        user.setProfileJson(profile);
        userRepository.save(user);
        return cart;
    }

    @Override
    @Transactional
    public CartResponseDto removeFromCart( CartRemoveRequestDto request) {
        Long userId= getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = user.getProfileJson();
        if (profile == null || !profile.containsKey("cart")) {
            throw new BadRequestException("Cart is empty");
        }

        CartResponseDto cart = objectMapper.convertValue(profile.get("cart"), CartResponseDto.class);
        if (cart.getItems() == null) throw new BadRequestException("Cart is empty");

        List<CartItemDto> updated = new ArrayList<>();
        for (CartItemDto item : cart.getItems()) {
            if (item.getItemId().equals(request.getItemId())) {
                int newQty = item.getQuantity() - 1;
                if (newQty > 0) {
                    item.setQuantity(newQty);
                    updated.add(item);
                }
            } else {
                updated.add(item);
            }
        }

        cart.setItems(updated);
        updateTotals(cart);
        profile.put("cart", cart);
        user.setProfileJson(profile);
        userRepository.save(user);
        return cart;
    }

    @Override
    @Transactional
    public void clearCart() {
        Long userId = getUserId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Map<String, Object> profile = user.getProfileJson() != null ? user.getProfileJson() : new HashMap<>();
        profile.remove("cart");
        user.setProfileJson(profile);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public CartResponseDto saveCart() {
        // For now, just return current cart
        return getCart();
    }

    private void updateTotals(CartResponseDto cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            cart.setSubtotal(BigDecimal.ZERO);
            cart.setTotalItems(0);
            return;
        }
        BigDecimal subtotal = cart.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubtotal(subtotal);
        cart.setTotalItems(cart.getItems().stream().mapToInt(CartItemDto::getQuantity).sum());
    }

    private CartResponseDto emptyCart() {
        return CartResponseDto.builder()
                .restaurantId(null)
                .items(new ArrayList<>())
                .subtotal(BigDecimal.ZERO)
                .totalItems(0)
                .build();
    }

    private Long getUserId(){
        return commonUtils.getUserId();
    }
}