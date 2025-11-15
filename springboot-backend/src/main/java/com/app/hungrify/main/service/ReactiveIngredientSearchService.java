package com.app.hungrify.main.service;


import com.app.hungrify.main.dto.search.ingredient.IngredientSuggestionDto;
import com.app.hungrify.main.models.Ingredient;
import com.app.hungrify.main.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactiveIngredientSearchService {

    private final IngredientRepository ingredientRepository;

    private static final int MIN_CONTAINS = 3;

    public Flux<IngredientSuggestionDto> suggest(String q, int limit) {
        String normalized = Optional.ofNullable(q).orElse("").trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) return Flux.empty();

        // Run blocking DB call on boundedElastic and convert results to Flux
        Mono<Page<Ingredient>> pageMono = Mono.fromCallable(() -> {
            Pageable pageable = PageRequest.of(0, Math.max(1, limit));
            if (normalized.length() < MIN_CONTAINS) {
                return ingredientRepository.findByNameStartingWithIgnoreCaseOrDisplayNameStartingWithIgnoreCase(normalized, normalized, pageable);
            } else {
                return ingredientRepository.findByNameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(normalized, normalized, pageable);
            }
        }).subscribeOn(Schedulers.boundedElastic());

        return pageMono.flatMapMany(page -> {
            // convert list -> sorted suggestions (score based)
            List<IngredientSuggestionDto> list = page.get()
                    .map(i -> toSuggestion(i, normalized))
                    .sorted(Comparator.comparingDouble(IngredientSuggestionDto::getScore).reversed())
                    .limit(limit)
                    .collect(Collectors.toList());
            return Flux.fromIterable(list);
        });
    }

    private IngredientSuggestionDto toSuggestion(Ingredient i, String q) {
        double score = computeScore(i, q);
        return IngredientSuggestionDto.builder()
                .ingredientId(i.getIngredientId())
                .name(i.getName())
                .displayName(i.getDisplayName())
                .allergens(i.getAllergens())
                .score(score)
                .build();
    }

    private double computeScore(Ingredient i, String q) {
        String name = Optional.ofNullable(i.getName()).orElse("").toLowerCase(Locale.ROOT);
        String display = Optional.ofNullable(i.getDisplayName()).orElse("").toLowerCase(Locale.ROOT);

        // token-aware: if each token is prefix of some word -> higher score
        String[] tokens = q.split("\\s+");
        boolean allTokensWordPrefix = Arrays.stream(tokens)
                .allMatch(tok -> wordPrefixMatch(name, display, tok));

        if (name.equals(q) || display.equals(q)) return 100.0;
        if (allTokensWordPrefix) return 95.0;
        if (name.startsWith(q)) return 90.0;
        if (display.startsWith(q)) return 80.0;
        if (name.contains(q)) return 60.0;
        if (display.contains(q)) return 50.0;
        return 10.0;
    }

    // returns true if token matches start of any word in name or display
    private boolean wordPrefixMatch(String name, String display, String token) {
        if (token.isBlank()) return false;
        String regex = "\\b" + java.util.regex.Pattern.quote(token);
        return name.matches("(?s).*" + regex + ".*") || display.matches("(?s).*" + regex + ".*");
    }
}