package com.example.RecomendationSystem.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.Enum.Type;
@Service
public class RecomendationService {
	
	private InterestScoringService interestScoringService;
	
	private MovieService movieService;
	
	private WatchedHistoryService historyService;
	
	public RecomendationService(MovieService movieService,InterestScoringService interestScoringService,
			WatchedHistoryService historyService) {
		this.interestScoringService = interestScoringService;
		this.movieService = movieService;
		this.historyService = historyService;
	}
	
	public List<MovieResponseDTO> createRecomendation(User user){
		List<UserPreference> userPreferences;
		userPreferences = interestScoringService.calculateInterest( user, new ArrayList<>() );
		Map<Type, Double> weights = userPreferences.stream()
				.collect( Collectors.toMap( UserPreference::getType, UserPreference::getWeight ) );
		List<Long> ids = historyService.getIdsWatchedMovies(user.getId());
		List<Movie> movies = movieService.getAllUnWatched(ids);
		
		return movies.stream().map(movie -> {
			double score = movie.getType().stream().mapToDouble(type -> weights.getOrDefault( type, 0.0 )).sum();
			return new MovieResponseDTO( movie.getId(), movie.getTitle(),
					movie.getDurationOfMovieSeconds(), movie.getRating(), score, movie.getType());
		}).sorted(Comparator.comparingDouble(MovieResponseDTO::weight).reversed()).limit( 20 ).toList();
	}
}
