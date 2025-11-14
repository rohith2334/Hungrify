package com.app.hungrify.main.controller;


import com.app.hungrify.main.dto.delivery.*;
import com.app.hungrify.main.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DeliveryController — handles partner-side delivery operations.
 */
@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
@Tag(name = "Deliveries", description = "Delivery partner endpoints")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "List current assigned deliveries")
    @GetMapping("/assigned")
    public ResponseEntity<List<DeliverySummaryDto>> getAssignedDeliveries() {
        return ResponseEntity.ok(deliveryService.getAssignedDeliveries());
    }

    @Operation(summary = "Accept or decline a delivery assignment")
    @PostMapping("/{deliveryId}/accept")
    public ResponseEntity<DeliveryActionResponseDto> acceptAssignment(
            @PathVariable Long deliveryId,
            @RequestParam Long partnerUserId,
            @Valid @RequestBody AcceptDeliveryRequestDto request) {
        return ResponseEntity.ok(deliveryService.acceptOrDecline(deliveryId, partnerUserId, request));
    }

    @Operation(summary = "Confirm pickup of order")
    @PostMapping("/{deliveryId}/confirm-pickup")
    public ResponseEntity<DeliveryActionResponseDto> confirmPickup(
            @PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryService.confirmPickup(deliveryId));
    }

    @Operation(summary = "Mark delivery as completed")
    @PostMapping("/{deliveryId}/mark-delivered")
    public ResponseEntity<DeliveryActionResponseDto> markDelivered(
            @PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryService.markDelivered(deliveryId));
    }

    @Operation(summary = "List delivery history for partner")
    @GetMapping("/history")
    public ResponseEntity<List<DeliverySummaryDto>> getHistory() {
        return ResponseEntity.ok(deliveryService.getHistory());
    }

    @Operation(summary = "Fetch partner's earnings summary")
    @GetMapping("/earnings")
    public ResponseEntity<EarningsSummaryDto> getEarnings(@RequestParam Long partnerUserId) {
        return ResponseEntity.ok(deliveryService.getEarnings(partnerUserId));
    }

    @Operation(summary = "Fetch full delivery detail")
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailDto> getDeliveryDetail(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(deliveryService.getDeliveryDetail(deliveryId));
    }
}
