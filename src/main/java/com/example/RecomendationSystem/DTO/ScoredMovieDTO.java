package com.example.RecomendationSystem.DTO;

import java.util.Map;

import com.example.RecomendationSystem.Entity.Movie;

public record ScoredMovieDTO(
	Movie movie,
	
	Map<String, Double> weights,
	
	double total
){}
