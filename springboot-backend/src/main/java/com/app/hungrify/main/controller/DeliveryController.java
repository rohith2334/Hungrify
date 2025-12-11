package com.app.hungrify.main.controller;


import com.app.hungrify.main.dto.delivery.*;
import com.app.hungrify.main.dto.user.UserProfileDto;
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

    @Operation(summary = "get all pending delivery assignments")
    @GetMapping("/pending")
    public ResponseEntity<List<DeliverySummaryDto>> getAssignedDeliveries() {
        return ResponseEntity.ok(deliveryService.getAssignedDeliveries());
    }

    // get all assigned deliveries for a specific partner
    @Operation(summary = "get all pending delivery assignments for a specific partner")
    @GetMapping("/assigned")
    public ResponseEntity<List<DeliverySummaryDto>> getAssignedDeliveriesForPartner(
    ) {
        return ResponseEntity.ok(deliveryService.getAssignedDeliveriesForPartner());
    }

    @Operation(summary = "Accept or decline a delivery assignment")
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<DeliveryActionResponseDto> acceptAssignment(
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok(deliveryService.accept(orderId));
    }

    @Operation(summary = "Reject a delivery assignment")
    @PostMapping("/{orderId}/reject")
    public ResponseEntity<DeliveryActionResponseDto> rejectAssignment(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.reject(orderId));
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

    //update online or offline status there is user attribute isActive togger tgat if this call is called
    @Operation(summary = "Update partner online/offline status")
    @PostMapping("/status")
    public ResponseEntity<UserProfileDto> updatePartnerStatus(
    ) {
        return ResponseEntity.ok(deliveryService.updatePartnerStatus());
    }

}
