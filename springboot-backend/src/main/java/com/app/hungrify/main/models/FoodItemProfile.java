package com.app.hungrify.main.models;

import com.app.hungrify.main.util.JsonListConverter;
import com.app.hungrify.main.util.JsonMapConverter;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "food_item_profiles",
        indexes = {
                @Index(name = "idx_profile_category", columnList = "category_code"),
                @Index(name = "idx_profile_spice", columnList = "spice_score"),
                @Index(name = "idx_profile_nutrition", columnList = "calories_kcal, protein_g, fats_g"),
                @Index(name = "idx_profile_vegan", columnList = "is_vegan"),
                @Index(name = "idx_profile_gluten_free", columnList = "is_gluten_free")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_profile_item"))
    private FoodItem item;

    @Column(length = 100)
    private String categoryCode;

    @Column(length = 200)
    private String categoryName;

    private Integer caloriesKcal;

    @Column(precision = 6, scale = 2)
    private BigDecimal carbsG;

    @Column(precision = 6, scale = 2)
    private BigDecimal proteinG;

    @Column(precision = 6, scale = 2)
    private BigDecimal fatsG;

    private Integer spiceScore; // 0..5

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SpiceLevel spiceLevel;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonListConverter.class)
    private List<String> allergens;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonListConverter.class)
    private List<String> tags;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> tasteProfile;

    // Derived booleans in SQL are stored as generated columns; map them read-only
    @Column(insertable = false, updatable = false)
    private Boolean hasMilk;

    @Column(insertable = false, updatable = false)
    private Boolean hasEggs;

    @Column(insertable = false, updatable = false)
    private Boolean hasFish;

    @Column(insertable = false, updatable = false)
    private Boolean hasShellfish;

    @Column(insertable = false, updatable = false)
    private Boolean hasTreeNuts;

    @Column(insertable = false, updatable = false)
    private Boolean hasPeanuts;

    @Column(insertable = false, updatable = false)
    private Boolean hasWheat;

    @Column(insertable = false, updatable = false)
    private Boolean hasSoy;

    @Column(insertable = false, updatable = false)
    private Boolean hasSesame;

    @Column(insertable = false, updatable = false)
    private Boolean isVegan;

    @Column(insertable = false, updatable = false)
    private Boolean isGlutenFree;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public enum SpiceLevel {
        none, mild, medium, spicy, very_spicy
    }
}
