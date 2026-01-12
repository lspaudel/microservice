package com.example.notification.config;

import com.example.shared.config.RabbitMQConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConsumerConfig {

    @Bean
    public Queue productCreatedQueue() {
        return new Queue(RabbitMQConfig.PRODUCT_CREATED_QUEUE, true);
    }

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(RabbitMQConfig.PRODUCT_EXCHANGE);
    }

    @Bean
    public Binding productCreatedBinding(Queue productCreatedQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productCreatedQueue)
                .to(productExchange)
                .with(RabbitMQConfig.PRODUCT_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
