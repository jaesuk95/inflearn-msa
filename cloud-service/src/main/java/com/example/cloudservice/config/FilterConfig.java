package com.example.cloudservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/first-service/**")
                        .filters(f->f.addRequestHeader("first-request","first-request-header")
                                .addResponseHeader("first-response", "first-response-header"))
                        .uri("http://localhost:8081/")) //route 객체에 path 정보 추가
                .route(r -> r.path("/second-service/**")
                        .filters(f->f.addRequestHeader("second-request","second-request-header")
                                .addResponseHeader("second-response", "second-response-header"))
                        .uri("http://localhost:8082/"))
                .build();
    }
//    @Bean
//    public ServerCodecConfigurer serverCodecConfigurer() {
//        return ServerCodecConfigurer.create();
//    }

//    @Bean
//    public RouteLocatorBuilder routeLocatorBuilder() {
//        return new RouteLocatorBuilder(context);
//    }
}
