//package com.example.RecomendationSystem.Impl.RankingEngine;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.example.RecomendationSystem.Entity.Movie;
//import com.example.RecomendationSystem.Entity.User;
//import com.example.RecomendationSystem.RankingEngine.GenerateCandidate;
//import com.example.RecomendationSystem.Repository.MovieRepository;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RequiredArgsConstructor
//@Service
//@Slf4j
//public class GenerateCandidateImpl implements GenerateCandidate{
//	
//	private final MovieRepository movieRepository; 
//	
//	@Override
//	public List<Movie> getCandidate(User user) {
//		log.atInfo().log("[GenerateCandidate] Getting candidate for {}", user.getUsername());
//		return movieRepository.getTopCandidte(user.getId());
//	}
//
//}
