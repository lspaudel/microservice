package com.example.shared.config;

/**
 * Shared RabbitMQ configuration constants.
 * Used by both publishers and consumers to ensure consistent naming.
 */
public class RabbitMQConfig {

    // Exchange names
    public static final String PRODUCT_EXCHANGE = "product-events-exchange";

    // Queue names
    public static final String PRODUCT_CREATED_QUEUE = "product-created-queue";

    // Routing keys
    public static final String PRODUCT_CREATED_ROUTING_KEY = "product.created";

    private RabbitMQConfig() {
        // Utility class
    }
}
