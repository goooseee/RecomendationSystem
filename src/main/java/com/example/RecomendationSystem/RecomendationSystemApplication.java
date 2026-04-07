package com.example.RecomendationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RecomendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecomendationSystemApplication.class, args);
	}

}
