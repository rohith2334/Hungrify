package com.app.hungrify.main.models.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderMeta {
    private Instant placedAt;
    private Instant confirmedAt;
    private Instant preparingAt;
    private Instant outForDeliveryAt;
    private Instant deliveredAt;
    private String note;
    private boolean deliveryPartnerAssigned;
    private List<Long> rejectedUsers;


    public Map<String, Object> toMap() {
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        if (placedAt != null) result.put("placed_at", placedAt);
        if (confirmedAt != null) result.put("confirmed_at", confirmedAt);
        if (preparingAt != null) result.put("preparing_at", preparingAt);
        if (outForDeliveryAt != null) result.put("out_for_delivery_at", outForDeliveryAt);
        if (deliveredAt != null) result.put("delivered_at", deliveredAt);
        if (note != null) result.put("note", note);
        return result;
    }

    public Map<String, String> timeStampMap() {
        return Map.of(
                "placedAt", placedAt.toString(),
                "confirmedAt", confirmedAt.toString(),
                "preparingAt", preparingAt.toString(),
                "outForDeliveryAt", outForDeliveryAt.toString(),
                "deliveredAt", deliveredAt.toString()
        );
    }
}
