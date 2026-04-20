package com.example.RecomendationSystem.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Clients.ScoringClient;
import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.DTO.RankingEngine.ScoringRequest;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Mapper.UserRecommendationProfileMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityOrchestrator {
	
	private final UserPreferenceService preferenceService;
	
	private final ScoringClient scoringClient;
	
	private final UserRecommendationProfileMapper mapper;
	
	private final RedisService redisService;
	
	private final MovieService movieService;
	
	private final WatchedHistoryService watchedHistoryService;
	
	private final UserService userService;
	@Transactional
	public void whenWatchedMovie(CreateHistoryRequestDTO requestDTO) {
		long id = userService.getUserIdFromContext();
		User user = userService.getUserById( id );
		
		Movie movie = movieService.getById( requestDTO.getMovieId() );
		
		watchedHistoryService.addHistory( user, requestDTO, movie );
		
		List<UserPreference> userPreferences = preferenceService.getByUserId( user.getId() );
		
		//System.out.println( userPreferences.get( 0 ).getType());
		
		List<WatchedHistory> histories = watchedHistoryService.getHistory( user.getId() );
//		System.out.println( mapper.toHistoryDtoList( histories ).toString() );
//		System.out.println( mapper.toProfile( user, userPreferences ).toString() );
//		System.out.println( new ScoringRequest( mapper.toHistoryDtoList( histories ),
//						mapper.toProfile( user, userPreferences ) ).toString() );
		
		preferenceService.saveUserPreference(scoringClient.calculate( 
				new ScoringRequest( mapper.toHistoryDtoList( histories ),
						mapper.toProfile( user, userPreferences ) ) ), user);
		
		histories.forEach( t -> t.setStatus( CountStatus.Count ) );
		
		watchedHistoryService.saveAll( histories );
		
		log.atDebug().log("Preference user = {} was update", user.getUsername());
		
		redisService.deleteDataRedis( user.getId() );
	}

}
