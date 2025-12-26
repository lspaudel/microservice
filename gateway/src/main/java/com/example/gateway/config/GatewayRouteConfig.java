package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

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

        @Bean
        public CorsWebFilter corsWebFilter() {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                corsConfig.setMaxAge(3600L);
                corsConfig.addAllowedMethod("*");
                corsConfig.addAllowedHeader("*");

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", corsConfig);

                return new CorsWebFilter(source);
        }
}
