package com.app.hungrify.main.dto.transaction;

import lombok.*;

/**
 * Filter params used for admin list view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilterRequestDto {
    private String status; // success, failed, refunded
    private String gateway;
    private String type;
    private Integer page;
    private Integer limit;
}