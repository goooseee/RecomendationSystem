package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.DTO.ScoredMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.RankingEngine.GenerateCandidate;
import com.example.RecomendationSystem.RankingEngine.RankingEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class RecomendationService {
	
	private final InterestScoringService interestScoringService;
	
	private final MovieService movieService;
	
	private final WatchedHistoryService historyService;
	
	private final UserPreferenceService preferenceService;
	
	private final GenerateCandidate genereteCandidate;
	
    private final RankingEngine rankingEngine;
	
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String RECS_KEY_PREFIX = "movie_recs::";
	
	public List<MovieResponseDTO> createRecomendation(@Param("user")User user){
		log.atInfo().log("[RecomendationService] requst for user = {}",user.getUsername() );
		long startTime = System.currentTimeMillis();
		String cacheKey = RECS_KEY_PREFIX + user.getId();
		try {
			log.atInfo().log("[RecomendationService] tries to get cache from Redis");
			List<MovieResponseDTO> cacheData = (List<MovieResponseDTO>)redisTemplate.opsForValue().get( cacheKey );
			if(cacheData!=null) {
				log.atInfo().log("[RecomendationService] CACHE HIT from Redis for user = {}", user.getUsername());
				return cacheData;
			}
		}
		catch (Exception e) {
			log.atError().log("[RecomendationService] Redis error = {}, swithcing to DB", e.getMessage());
		}
		List<UserPreference> userPreferences = preferenceService.getByUserId( user.getId() );
		Map<Type, Double> weights = userPreferences.stream()
				.collect( Collectors.toMap( UserPreference::getType, UserPreference::getWeight ) );
		log.atDebug().log("[RecomendationService] Getting weights = {} for user = {}", weights, user.getId());
		List<Long> ids = historyService.getIdsWatchedMovies(user.getId());
		List<Movie> movies = movieService.getAllUnWatched(ids);
		log.atDebug().log("[RecomendationService] Getting candidates = {}", movies.size());
		
		List<MovieResponseDTO> result = movies.stream().map(movie -> {
			
		    List<Type> detachedGenres = List.copyOf(movie.getType()); 
		    
		    double score = detachedGenres.stream()
		            .mapToDouble(type -> weights.getOrDefault(type, 0.0))
		            .sum();
		    
		    log.atTrace().log("[RecomendationService] Set movie = {} score = {}", movie.getId(), score);
		    
		    return new MovieResponseDTO(
		            movie.getId(),
		            movie.getTitle(),
		            movie.getDurationOfMovieSeconds(),
		            movie.getRating(),
		            score,
		            detachedGenres 
		    );
		})
		.sorted(Comparator.comparingDouble(MovieResponseDTO::weight).reversed())
		.limit(20)
		.collect( Collectors.toList() );
		try {
			redisTemplate.opsForValue().set( cacheKey, result, Duration.ofMinutes( 10 ) );
			log.atInfo().log("[RecomendationService] Result saved in Redis for user = {}",user.getUsername());
		}
		catch (Exception e) {
			log.atWarn().log("[RecomendationService] Could not to save in Redis: {}", e.getMessage());
		}
		log.atInfo().log("[RecomendationService] response for user = {} in ms = {}",user.getUsername(), System.currentTimeMillis()-startTime );
		return result;
	}
	
	public List<MovieResponseDTO> getRecommend(User user){
		
		List<Movie> candidates = genereteCandidate.getCandidate( user );
		
		List<ScoredMovieDTO> scoredMovie = rankingEngine.calculateWeights( user, candidates );
		
		return scoredMovie.stream().map( this::convert ).toList();
	}
	
	private MovieResponseDTO convert (ScoredMovieDTO scoredMovieDTO) {
		Movie movie = scoredMovieDTO.movie();
		return new MovieResponseDTO( movie.getId(),
				movie.getTitle(),
				movie.getDurationOfMovieSeconds(),
				movie.getRating(),
				scoredMovieDTO.total(),
				movie.getType() );
	}
}
