package com.example.RecomendationSystem.MockTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Service.InterestScoringService;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.RedisService;
import com.example.RecomendationSystem.Service.UserActivityOrchestrator;
import com.example.RecomendationSystem.Service.UserPreferenceService;
import com.example.RecomendationSystem.Service.UserService;
import com.example.RecomendationSystem.Service.WatchedHistoryService;
@ExtendWith(MockitoExtension.class)
public class UserActivityOrchestratorTest {
	@Mock
	private UserPreferenceService preferenceService;
	@Mock
	private InterestScoringService interestScoringService;
	@Mock
	private RedisService redisService;
	@Mock
	private MovieService movieService;
	@Mock
	private WatchedHistoryService watchedHistoryService;
	@Mock
	private UserService userService;
	@InjectMocks
	private UserActivityOrchestrator orchestrator;
	@Test
	void whenWatchedMovieTest() {
		CreateHistoryRequestDTO requestDTO = new CreateHistoryRequestDTO();
		requestDTO.setMovieId( 1L );
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		when(userService.getUserFromContext()).thenReturn( user );
		when(movieService.getById( 1L )).thenReturn(new Movie());
		when(preferenceService.getByUserId( 1L )).thenReturn( List.of(new UserPreference()) );
		when(interestScoringService.calculateInterest( any(), any(), any() )).thenReturn( List.of(new UserPreference()) );
		doNothing().when(preferenceService).saveUserPreference( any() );
		doNothing().when( redisService ).deleteDataRedis( any() );
		
		orchestrator.whenWatchedMovie( requestDTO );
		verify( userService ).getUserFromContext();
		verify( movieService ).getById( 1 );
		verify( preferenceService ).getByUserId( 1L );
		verify( preferenceService ).saveUserPreference( any() );
		verify( redisService ).deleteDataRedis( any() );
	}
}
