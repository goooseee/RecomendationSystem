package com.example.RecomendationSystem.RankingEngine;

import java.util.List;

import com.example.RecomendationSystem.DTO.ScoredMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;

public interface RankingEngine {
	
	public List<ScoredMovieDTO> calculateWeights(User user, List<Movie> candidates);
	
}
