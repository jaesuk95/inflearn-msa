package com.example.cloudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudServiceApplication.class, args);
	}

	// httptrace 를 위한 bean 등록
//	@Bean
//	public HttpExchangeRepository httpTraceRepository() {
//		return new InMemoryHttpExchangeRepository();
//	}
	// In Spring Boot, the httptrace endpoint provides
	// tracing information for HTTP requests that are
	// made to the Spring Boot application.
	//
	//When you enable the httptrace endpoint by including
	// it in the exposure section of the endpoints
	// configuration, Spring Boot creates an in-memory
	// circular buffer that contains information about
	// recent HTTP requests and responses. This information
	// includes details such as the request method, status
	// code, request and response headers, and timings.
	//
	//The tracehttp option is not a valid Spring Boot
	// management option, but it may be a typo of httptrace.
	// By including httptrace in the exposure section of the
	// endpoints configuration, you are allowing access to
	// the httptrace endpoint over HTTP. This means that
	// you can use tools like curl or a web browser to
	// retrieve tracing information about recent HTTP
	// requests and responses made to your Spring Boot
	// application.

	// example
	// {
	//  "traces": [
	//    {
	//      "timestamp": "2022-04-28T15:10:38.245Z",
	//      "principal": null,
	//      "session": null,
	//      "request": {
	//        "method": "GET",
	//        "uri": "http://localhost:8080/api/books",
	//        "headers": {
	//          "Accept": [
	//            "application/json"
	//          ],
	//          "User-Agent": [
	//            "curl/7.68.0"
	//          ],
	//          "Host": [
	//            "localhost:8080"
	//          ],
	//          "Connection": [
	//            "Keep-Alive"
	//          ]
	//        },
	//        "remoteAddress": "127.0.0.1",
	//        "remoteUser": null
	//      },
	//      "response": {
	//        "status": 200,
	//        "headers": {
	//          "Content-Type": [
	//            "application/json"
	//          ]
	//        }
	//      },
	//      "timeTaken": 2
	//    }
	//  ]
	//}
}
