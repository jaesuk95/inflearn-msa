package com.example.cloudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableDiscoveryClient
@ConfigurationPropertiesScan    // 스캔 방식으로 프로퍼티 클래스들을 등록하는 방법
public class CloudServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CloudServiceApplication.class, args);
	}

}
