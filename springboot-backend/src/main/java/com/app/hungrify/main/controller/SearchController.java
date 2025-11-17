package com.app.hungrify.main.controller;


import com.app.hungrify.main.dto.search.SearchResponseDto;
import com.app.hungrify.main.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Validated
@Tag(name = "Search", description = "Combined restaurant and food item search")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search across restaurants and food items")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search results grouped by restaurant"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public ResponseEntity<SearchResponseDto> search(
            @Parameter(description = "Search query text") @RequestParam String q,
            @Parameter(description = "City filter (optional)") @RequestParam(required = false) String city,
            @Parameter(description = "Ai search boolean") @RequestParam(required = false) boolean AIFlag
    ) {

        return ResponseEntity.ok(searchService.search(q, city, AIFlag));
    }
}