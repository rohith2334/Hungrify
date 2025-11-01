package com.app.hungrify.main.models;

import com.app.hungrify.common.models.Users;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "deliveries",
        indexes = {
                @Index(name = "idx_delivery_partner", columnList = "partner_user_id"),
                @Index(name = "idx_delivery_status", columnList = "status")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_delivery_order"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_user_id", foreignKey = @ForeignKey(name = "fk_delivery_partner"))
    private Users partnerUser; // delivery partner, nullable

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private VehicleType partnerVehicleType;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private DeliveryStatus status = DeliveryStatus.assigned;

    private Integer estimatedTimeMinutes;

    private Instant actualDeliveryTime;

    @Column(columnDefinition = "json")
    private String deliveryMeta;

    @CreationTimestamp
    private Instant createdAt;

    public enum VehicleType { bike, car, bicycle, other }
    public enum DeliveryStatus { assigned, picked_up, delivered, cancelled }
}

