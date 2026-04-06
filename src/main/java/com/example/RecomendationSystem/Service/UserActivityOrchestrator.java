package com.example.RecomendationSystem.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityOrchestrator {
	
	private final UserPreferenceService preferenceService;
	
	private final InterestScoringService interestScoringService;
	
	private final RedisService redisService;
	
	private final MovieService movieService;
	
	private final WatchedHistoryService watchedHistoryService;
	@Transactional
	public void whenWatchedMovie(CreateHistoryRequestDTO requestDTO) {
		//позже добавлю в userservice метод что будет доставать из контекста пользователя
		User user = new User();
		
		Movie movie = movieService.getById( requestDTO.getMovieId() );
		
		watchedHistoryService.addHistory( user, requestDTO, movie );
		
		List<UserPreference> userPreferences = preferenceService.getByUserId( user.getId() );
		
		preferenceService.saveUserPreference( interestScoringService.calculateInterest( user, userPreferences, watchedHistoryService.getHistory( user.getId() ) ) );
		
		log.atDebug().log("Preference user = {} was update", user.getUsername());
		
		redisService.deleteDataRedis( user.getId() );
	}

}
