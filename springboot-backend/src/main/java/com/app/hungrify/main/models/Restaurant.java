package com.app.hungrify.main.models;


import com.app.hungrify.common.models.Users;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "restaurants",
        indexes = {
                @Index(name = "idx_restaurant_city", columnList = "city"),
                @Index(name = "idx_restaurant_active", columnList = "isActive")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restaurant_owner"))
    private Users owner;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String cuisine;

    @Lob
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 30)
    private String postalCode;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(columnDefinition = "json")
    private String restaurantMeta;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    // convenience - back reference
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<FoodItem> foodItems;
}
