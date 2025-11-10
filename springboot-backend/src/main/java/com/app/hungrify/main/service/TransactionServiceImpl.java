package com.app.hungrify.main.service;

import com.app.hungrify.main.dto.transaction.*;
import com.app.hungrify.main.exception.NotFoundException;
import com.app.hungrify.main.models.Order;
import com.app.hungrify.main.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Uses orders.payment_* fields as source for transactions.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionSummaryDto> listTransactions(TransactionFilterRequestDto filter) {
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getLimit(), Sort.by(Sort.Direction.DESC, "createdAt"));

        // Simplified filtering (since transactions are in orders table)
        List<Order> orders = orderRepository.findAll(pageable).getContent();
        return orders.stream()
                .filter(o -> o.getPaymentTransactionRef() != null)
                .filter(o -> filter.getStatus() == null || o.getPaymentStatus().name().equalsIgnoreCase(filter.getStatus()))
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDetailDto getTransactionDetail(String txnId) {
        Order order = orderRepository.findByPaymentTransactionRef(txnId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        return toDetail(order);
    }


    // Helpers
    private TransactionSummaryDto toSummary(Order o) {
        return TransactionSummaryDto.builder()
                .transactionId(o.getPaymentTransactionRef())
                .orderId(o.getOrderId())
                .userId(o.getUser().getUserId())
                .amount(o.getTotalAmount())
                .status(o.getPaymentStatus().name())
                .gateway(o.getPaymentMeta() != null ? (String) o.getPaymentMeta().getOrDefault("gateway", "N/A") : "N/A")
                .type("order_payment")
                .createdAt(o.getCreatedAt())
                .build();
    }

    private TransactionDetailDto toDetail(Order o) {
        return TransactionDetailDto.builder()
                .transactionId(o.getPaymentTransactionRef())
                .orderId(o.getOrderId())
                .userId(o.getUser().getUserId())
                .amount(o.getTotalAmount())
                .status(o.getPaymentStatus().name())
                .gateway(o.getPaymentMeta() != null ? (String) o.getPaymentMeta().getOrDefault("gateway", "N/A") : "N/A")
                .type("order_payment")
                .rawResponse(o.getPaymentMeta())
                .createdAt(o.getCreatedAt())
                .build();
    }
}