package com.app.hungrify.main.models;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "food_items",
        indexes = {
                @Index(name = "idx_fooditem_price", columnList = "price"),
                @Index(name = "idx_fooditem_restaurant", columnList = "restaurant_id"),
                @Index(name = "idx_fooditem_rating", columnList = "rating")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fooditem_rest"))
    private Restaurant restaurant;

    @Column(nullable = false, length = 250)
    private String canonicalName;

    @Column(nullable = false, length = 250)
    private String displayName;

    @Column(length = 512)
    private String shortDescription;

    @Lob
    private String longDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private Integer quantity = 1;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    private Integer prepTimeMinutes;

    @Column(columnDefinition = "json")
    private String imageUrls; // JSON array

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private FoodItemProfile profile;

    @OneToMany(mappedBy = "foodItem", fetch = FetchType.LAZY)
    private List<FoodItemIngredient> ingredients;
}

