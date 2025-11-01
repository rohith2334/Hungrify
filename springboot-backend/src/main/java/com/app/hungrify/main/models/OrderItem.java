package com.app.hungrify.main.models;


import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "order_items",
        indexes = {
                @Index(name = "idx_orderitem_order", columnList = "order_id"),
                @Index(name = "idx_orderitem_item", columnList = "item_id")
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orderitem_order"))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_orderitem_item"))
    private FoodItem item;

    private Integer quantity = 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(columnDefinition = "json")
    private String customizationSelected;

    @Column(columnDefinition = "json", nullable = false)
    private String itemSnapshot; // snapshot of item at time of order; JSON

    @CreationTimestamp
    private Instant createdAt;
}

