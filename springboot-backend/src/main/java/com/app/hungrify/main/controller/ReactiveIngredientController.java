package com.app.hungrify.main.controller;


import com.app.hungrify.main.dto.search.ingredient.IngredientSuggestionDto;
import com.app.hungrify.main.service.ReactiveIngredientSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class ReactiveIngredientController {

    private final ReactiveIngredientSearchService service;

    /**
     * Stream suggestions for a single query.
     * Returns a Flux of suggestions (streamed). Client can subscribe and render as they arrive.
     */
    @GetMapping(value = "/suggest", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IngredientSuggestionDto> suggest(@RequestParam("q") String q,
                                                 @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return service.suggest(q, limit);
    }
}