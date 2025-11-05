package com.app.hungrify.main.models;

import com.app.hungrify.main.util.JsonListConverter;
import com.app.hungrify.main.util.JsonMapConverter;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
    @Convert(converter = JsonListConverter.class)
    private List<String> allergens;   // JSON array

    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> meta;

    @CreationTimestamp
    private Instant createdAt;
}

