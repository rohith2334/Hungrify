package com.app.hungrify.main.models;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "food_item_ingredients",
        indexes = {
                @Index(name = "idx_fi_item", columnList = "item_id"),
                @Index(name = "idx_fi_ingredient", columnList = "ingredient_id")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fi_item"))
    private FoodItem foodItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fi_ingredient"))
    private Ingredient ingredient;

    private Boolean removable = false;

    @Column(columnDefinition = "json")
    private String removalEffects;

    @CreationTimestamp
    private Instant createdAt;
}
