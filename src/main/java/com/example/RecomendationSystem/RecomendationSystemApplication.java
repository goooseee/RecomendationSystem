package com.example.RecomendationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = "com.example.RecomendationSystem")
@EnableFeignClients
public class RecomendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecomendationSystemApplication.class, args);
	}

}
