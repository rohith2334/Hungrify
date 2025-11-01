package com.app.hungrify.main.models;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "ingredients",
        indexes = {@Index(name = "idx_ingredient_name", columnList = "name")})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @Column(length = 200)
    private String displayName;

    @Column(columnDefinition = "json")
    private String allergens;   // JSON array

    @Column(columnDefinition = "json")
    private String meta;

    @CreationTimestamp
    private Instant createdAt;
}

