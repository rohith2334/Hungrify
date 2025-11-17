package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.search.SearchResponseDto;

/**
 * Service interface for search across restaurants and food items.
 */
public interface SearchService {
    /**
     * Combined search for restaurants and food items.
     *
     * @param query search text
     * @param city  optional city filter
     * @param AIFlag
     * @return grouped search results
     */
    SearchResponseDto search(String query, String city, boolean AIFlag);
}
