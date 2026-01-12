package com.example.core.events;

import com.example.core.entity.Product;
import com.example.shared.config.RabbitMQConfig;
import com.example.shared.events.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Service responsible for publishing product-related events to RabbitMQ.
 */
@Service
@Slf4j
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes a ProductCreatedEvent when a new product is created.
     * This is an asynchronous operation that doesn't block the main thread.
     */
    public void publishProductCreated(Product product) {
        ProductCreatedEvent event = new ProductCreatedEvent(
                product.getId(),
                product.getName(),
                BigDecimal.valueOf(product.getPrice()),
                product.getQuantity(),
                Instant.now());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PRODUCT_EXCHANGE,
                RabbitMQConfig.PRODUCT_CREATED_ROUTING_KEY,
                event);

        log.info("Published ProductCreatedEvent for product ID: {}, name: {}",
                product.getId(), product.getName());
    }
}
