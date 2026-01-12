package com.example.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Event published when a new product is created.
 * This event is consumed by the Notification service to send notifications.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer stock;
    private Instant timestamp;
}
