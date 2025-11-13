package com.app.hungrify.main.controller;

import com.app.hungrify.main.dto.transaction.*;
import com.app.hungrify.main.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Transaction Controller — payment logs overview and management.
 */
@RestController
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
@Validated
@Tag(name = "Admin - Transactions", description = "View and manage payment transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "List all payment transactions (paginated)")
    @GetMapping
    public ResponseEntity<List<TransactionSummaryDto>> getTransactions(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String gateway,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {

        TransactionFilterRequestDto filter = TransactionFilterRequestDto.builder()
                .status(status)
                .gateway(gateway)
                .type(type)
                .page(page)
                .limit(limit)
                .build();

        return ResponseEntity.ok(transactionService.listTransactions(filter));
    }

    @Operation(summary = "Fetch full transaction log")
    @GetMapping("/{txnId}")
    public ResponseEntity<TransactionDetailDto> getTransactionDetail(@PathVariable String txnId) {
        return ResponseEntity.ok(transactionService.getTransactionDetail(txnId));
    }


}
