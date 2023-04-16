package com.example.cloudservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                //first-service
                .route(r -> r
                        .path("/first-service/**")
                        .uri("http://localhost:8081"))
                //second-service
                .route(r -> r
                        .path("/second-service/**")
//                        .filters(f -> f.addResponseHeader("second-request", "second-request-header")
//                                .addResponseHeader("second-response", "second-response-header"))
                        .uri("http://localhost:8082"))
                .build();
    }
}
