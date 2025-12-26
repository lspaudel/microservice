package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {
        @Bean
        public RouteLocator routeLocator(RouteLocatorBuilder builder) {
                return builder.routes()
                                .route(r -> r.path("/core/**")
                                                .filters(f -> f.rewritePath("/core/(?<remains>.*)", "/${remains}")
                                                                .circuitBreaker(c -> c.setName("core").setFallbackUri(
                                                                                "forward:/fallback/core")))
                                                .uri("lb://CORE")) // Use Eureka service ID here

                                .route(r -> r.path("/auth/**")
                                                .filters(f -> f.rewritePath("/auth/(?<remains>.*)", "/${remains}")
                                                                .circuitBreaker(c -> c.setName("auth").setFallbackUri(
                                                                                "forward:/fallback/auth")))
                                                .uri("lb://AUTH"))
                                .route(r -> r.path("/api/notification/**")
                                                .uri("lb://NOTIFICATION"))
                                .build();


    }
}
