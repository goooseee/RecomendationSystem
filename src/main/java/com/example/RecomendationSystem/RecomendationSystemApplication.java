package com.example.RecomendationSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RecomendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecomendationSystemApplication.class, args);
	}

}
