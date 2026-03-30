package com.example.RecomendationSystem.RankingEngine;

import java.util.List;

import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.MovieRepository;

public interface GenerateCandidate {
	
	public List<Movie> getCandidate(User user);
	
}
