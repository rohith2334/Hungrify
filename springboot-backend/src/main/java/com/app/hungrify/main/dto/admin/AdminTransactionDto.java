package com.app.hungrify.main.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminTransactionDto {
    private Long id;
    private String type;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Long userId;
    private Long orderId;
    private String amount;
}
