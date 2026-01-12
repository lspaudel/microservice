package com.example.core.config;

import com.example.shared.config.RabbitMQConfig;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for Core service (Publisher).
 * Creates the exchange for publishing product events.
 */
@Configuration
public class RabbitMQPublisherConfig {

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(RabbitMQConfig.PRODUCT_EXCHANGE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
