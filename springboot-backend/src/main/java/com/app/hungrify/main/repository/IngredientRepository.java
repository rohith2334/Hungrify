package com.app.hungrify.main.repository;



import com.app.hungrify.main.models.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
    Optional<Ingredient> findByNameIgnoreCase(String name);

    @Query("SELECT i.name FROM Ingredient i")
    List<String> findAllIngredientNames();

    Page<Ingredient> findByNameStartingWithIgnoreCaseOrDisplayNameStartingWithIgnoreCase(
            String namePrefix,
            String displayNamePrefix,
            Pageable pageable
    );

    /**
     * Contains search → used when input length ≥ MIN_CONTAINS
     */
    Page<Ingredient> findByNameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(
            String name,
            String displayName,
            Pageable pageable
    );
}
