package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@LoadBalanced // 사용 이유 : order_service: url: http://order-service
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	// @LoadBalanced 사용 이유:
	// order_service:
	//  url: http://127.0.0.1:8000/order-service/%s/orders -> http://order-service/order-service/%s/orders 으로 변경

	// RestTemplate is a class in the Spring Framework that makes it easier to make
	// HTTP requests and consume RESTful web services from within a Spring application.
	// It simplifies the process of interacting with RESTful web services by providing
	// a high-level, easy-to-use API for making HTTP requests and handling responses.
	//
	//With RestTemplate, you can perform operations like GET, POST, PUT, DELETE, etc.
	// on a RESTful web service endpoint. It allows you to specify request headers,
	// query parameters, request bodies, and response types, among other things.
	//
	//RestTemplate also supports several message converters, which convert between
	// Java objects and the HTTP message body. This makes it easy to serialize and
	// deserialize JSON, XML, and other types of data.
	//
	//To use RestTemplate in a Spring application, you can simply inject it into your
	// code using the @Autowired annotation. Then, you can call its methods to perform
	// HTTP requests and handle the responses.
	//
	//Overall, RestTemplate is a powerful tool that makes it easy to consume RESTful
	// web services in a Spring application.
}
