package com.example.notification.listener;

import com.example.shared.config.RabbitMQConfig;
import com.example.shared.events.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {

    @RabbitListener(queues = RabbitMQConfig.PRODUCT_CREATED_QUEUE)
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("Received ProductCreatedEvent: {}", event);

        // Simulating sending email
        log.info("Sending notification email for new product: {} (Price: {})",
                event.getProductName(), event.getPrice());

        // Actual email logic would go here, reusing existing EmailService if available
    }
}
