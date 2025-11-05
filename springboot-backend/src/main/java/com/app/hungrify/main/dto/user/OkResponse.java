package com.app.hungrify.main.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic OK response wrapper with payload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OkResponse<T> {
    private String status;
    private T payload;
}
