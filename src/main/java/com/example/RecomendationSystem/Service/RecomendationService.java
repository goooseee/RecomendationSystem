package com.example.RecomendationSystem.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.repository.query.Param;
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
	
	private final GenerateCandidate generateCandidate;
	
    private final RankingEngine rankingEngine;
    
    private final RedisService redisService;
    
	public List<MovieResponseDTO> getRecommend(User user){	
		
		List<MovieResponseDTO> scoredMovieDTO = redisService.getDataFromRedis( user.getId() );
		
		if(scoredMovieDTO != null) {
			log.atInfo().log("[RecomendationService] Cache HIT for user {}", user.getId());
			return scoredMovieDTO;
		}
		
		log.atInfo().log("[RecomendationService] Cache MISS for user {}. Calculating recommendations...", user.getId());
		
		List<Movie> candidates = generateCandidate.getCandidate( user );
		
		List<ScoredMovieDTO> scoredMovie = rankingEngine.calculateWeights( user, candidates );
		
		List<MovieResponseDTO> movieResponseDTO = scoredMovie.stream().map( this::convert ).toList();
		
		redisService.addDataToRedis( user.getId(), movieResponseDTO );
		
		return movieResponseDTO;
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
