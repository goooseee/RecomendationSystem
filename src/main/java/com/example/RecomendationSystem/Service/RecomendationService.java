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

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class RecomendationService {
	
	private InterestScoringService interestScoringService;
	
	private MovieService movieService;
	
	private WatchedHistoryService historyService;
	
	private UserPreferenceService preferenceService;
	
	public RecomendationService(MovieService movieService,InterestScoringService interestScoringService,
			WatchedHistoryService historyService,UserPreferenceService preferenceService) {
		this.interestScoringService = interestScoringService;
		this.movieService = movieService;
		this.historyService = historyService;
		this.preferenceService = preferenceService;
	}
	
	public List<MovieResponseDTO> createRecomendation(User user){
		log.atInfo().log( "Making recomendation for user with username = {}",user.getUsername() );
		long startTime = System.currentTimeMillis();
		List<UserPreference> userPreferences = preferenceService.getByUserId( user.getId() );
		Map<Type, Double> weights = userPreferences.stream()
				.collect( Collectors.toMap( UserPreference::getType, UserPreference::getWeight ) );
		log.atDebug().log( "Getting weight = {} users = {}", weights, user.getId());
		List<Long> ids = historyService.getIdsWatchedMovies(user.getId());
		List<Movie> movies = movieService.getAllUnWatched(ids);
		log.atDebug().log("Getting movies that can be recomend = {}", movies);
		
		List<MovieResponseDTO> result = movies.stream().map(movie -> {
			double score = movie.getType().stream().mapToDouble(type -> weights.getOrDefault( type, 0.0 )).sum();
			log.atTrace().log("Set movie = {} score = {}", movie.toString(), score);
			
			return new MovieResponseDTO( movie.getId(), movie.getTitle(),
					movie.getDurationOfMovieSeconds(), movie.getRating(), score, movie.getType());
		}).sorted(Comparator.comparingDouble(MovieResponseDTO::weight).reversed()).limit( 20 ).toList();
		log.atDebug().log("Recomendation for user = {} completed in ms = {}", user.getUsername(), System.currentTimeMillis() - startTime);
		return result;
	}
}
