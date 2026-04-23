package com.example.RecomendationSystem.Clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.RecomendationSystem.DTO.CreateHistoryDTORequest;
import com.example.RecomendationSystem.DTO.CreateMovieDTORequest;
import com.example.RecomendationSystem.DTO.ResponseDTO;
import com.example.RecomendationSystem.DTO.RankingEngine.ScoringRequest;
import com.example.RecomendationSystem.DTO.RankingEngine.UserPreferenceDTO;

@FeignClient(name = "ranking-engine", url = "http://localhost:8081")
public interface ScoringClient {
	
	@PostMapping("/api/v1/scoring/calculate")
    void calculate(@RequestBody Long userId);
	
	@GetMapping("/api/v1/scoring/getRecommend/{userId}")
	ResponseDTO getRecommend(@PathVariable("userId") long userId);
	
	@PostMapping("/api/v1/scoring/addMovie")
	void addMovie(@RequestBody CreateMovieDTORequest dto);
	
	@PostMapping("/api/v1/scoring/addHistory")
	void createHistory(@RequestBody CreateHistoryDTORequest dto);
}
