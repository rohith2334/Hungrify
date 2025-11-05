package com.app.hungrify.main.controller;

// package com.app.hungrify.main.controller;

import com.app.hungrify.main.dto.user.RestaurantWithFoodsDto;
import com.app.hungrify.main.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Search", description = "Search restaurants and foods")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Combined search for foods grouped by restaurant")
    @GetMapping("/search")
    public ResponseEntity<List<RestaurantWithFoodsDto>> search(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {

        List<RestaurantWithFoodsDto> results = searchService.searchFoodsGrouped(q, city, page, limit);
        return ResponseEntity.ok(results);
    }
}
