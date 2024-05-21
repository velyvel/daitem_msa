package com.api_gateway.common;

//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("user-service", r -> r
//                        .path("/api/v1/user/**",
//                                "/api/v1/user-order/**",
//                                "/api/v1/my-page/**")
//                        .uri("http://localhost:8080"))
//                .route("product-service", r -> r
//                        .path("/api/v1/product/**",
//                                "/api/v1/product-detail/**",
//                                "/api/v1/cart/**",
//                                "/api/v1/order-to-product/**")
//                        .uri("http://localhost:8081"))
//                .route("order-service", r -> r
//                        .path("/api/v1/order/**",
//                                "/api/v1/order-detail/**",
//                                "/api/v1/delivery/**")
//                        .uri("http://localhost:8082"))
//                .build();
//    }
}
