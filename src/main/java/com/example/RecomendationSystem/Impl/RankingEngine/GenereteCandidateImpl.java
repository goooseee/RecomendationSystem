package com.example.RecomendationSystem.Impl.RankingEngine;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.RankingEngine.GenereteCandidate;
import com.example.RecomendationSystem.Repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GenereteCandidateImpl implements GenereteCandidate{
	
	private final MovieRepository movieRepository; 
	
	@Override
	public List<Movie> getCandidate(User user) {
		return movieRepository.getTopCandidte(user.getId());
	}

}
