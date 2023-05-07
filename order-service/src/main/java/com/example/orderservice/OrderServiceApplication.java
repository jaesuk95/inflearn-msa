package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		Hooks.enableContextLossTracking();
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
