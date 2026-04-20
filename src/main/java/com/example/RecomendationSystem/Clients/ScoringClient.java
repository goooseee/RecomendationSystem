package com.example.RecomendationSystem.Clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.RecomendationSystem.DTO.RankingEngine.ScoringRequest;
import com.example.RecomendationSystem.DTO.RankingEngine.UserPreferenceDTO;

@FeignClient(name = "ranking-engine", url = "http://localhost:8081")
public interface ScoringClient {
	
	@PostMapping("/api/v1/scoring/calculate")
    List<UserPreferenceDTO> calculate(@RequestBody ScoringRequest request);
	
}
