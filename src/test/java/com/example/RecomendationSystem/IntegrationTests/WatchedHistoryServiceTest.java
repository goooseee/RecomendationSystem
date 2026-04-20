package com.example.RecomendationSystem.IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Repository.UserPreferenceRepository;
import com.example.RecomendationSystem.Repository.WatchedHistoryRepository;
import com.example.RecomendationSystem.Service.WatchedHistoryService;
@DataJpaTest
@ActiveProfiles("test")
public class WatchedHistoryServiceTest {
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private WatchedHistoryRepository historyRepository;
	
	private WatchedHistoryService historyService;
	
	private User testUser;
	private Movie testMovie;
	private Movie testMovie1;
	
	@BeforeEach
	void setUp() {
	    historyService = new WatchedHistoryService(historyRepository);

	    testUser = User.builder()
	            .username("ivan")
	            .build();
	    testUser = entityManager.persistFlushFind(testUser); 

	    testMovie = Movie.builder()
	            .title("Inception")
	            .build();
	    testMovie = entityManager.persistFlushFind(testMovie);
	    
	    testMovie1 = Movie.builder()
	            .title("Inception1")
	            .build();
	    testMovie1 = entityManager.persistFlushFind(testMovie1);
	}
	@Test
	void shouldAddHistory() {
		CreateHistoryRequestDTO requestDTO = new CreateHistoryRequestDTO();
		requestDTO.setReact( Reaction.liked );
		requestDTO.setSecondsWatched( 12 );
		
		historyService.addHistory( testUser, requestDTO, testMovie );
		List<WatchedHistory> watchedHistories = historyService.getHistory( testUser.getId() );
		
		assertEquals( 1, watchedHistories.size() );
		assertEquals( CountStatus.NotCount, watchedHistories.get( 0 ).getStatus() );
		assertEquals( testMovie.getId(), watchedHistories.get( 0 ).getMovie().getId() );
		assertEquals("Inception", watchedHistories.get(0).getMovie().getTitle());
	}
	@Test
	void shouldGetHistoryForScoring() {
		CreateHistoryRequestDTO requestDTO = new CreateHistoryRequestDTO();
		requestDTO.setReact( Reaction.liked );
		requestDTO.setSecondsWatched( 12 );
		
		historyService.addHistory( testUser, requestDTO, testMovie );
		List<WatchedHistory> watchedHistories = historyService.getHistoryForScorting( testUser.getId() );
		
		assertEquals( 1, watchedHistories.size() );
		assertEquals( CountStatus.NotCount, watchedHistories.get( 0 ).getStatus() );
	}
	@Test
	void shouldGetIdsWatchedMovies() {
		CreateHistoryRequestDTO requestDTO = new CreateHistoryRequestDTO();
		requestDTO.setReact( Reaction.liked );
		requestDTO.setSecondsWatched( 12 );
		WatchedHistory history = WatchedHistory.builder()
				.durationSeconds(requestDTO.getSecondsWatched())
				.movie( testMovie1 )
				.react( requestDTO.getReact() )
				.status( CountStatus.Count )
				.user( testUser )
				.timesWatched( 1 )
				.whenWatched( LocalDate.now() )
				.build();
		historyRepository.save( history );
		historyService.addHistory( testUser, requestDTO, testMovie );
		List<Long> ids = historyService.getIdsWatchedMovies( testUser.getId() );
		
		assertEquals( 2, ids.size() );
	}
	@Test
	void shouldDeleteHistoryById() {
		CreateHistoryRequestDTO requestDTO = new CreateHistoryRequestDTO();
		requestDTO.setReact( Reaction.liked );
		requestDTO.setSecondsWatched( 12 );
		
		historyService.addHistory( testUser, requestDTO, testMovie );
		historyService.addHistory( testUser, requestDTO, testMovie1 );
		
		historyService.deleteById( testMovie.getId(), testUser.getId() );
		
		entityManager.flush(); 
	    entityManager.clear();
		
		List<WatchedHistory> watchedHistories = historyService.getHistory( testUser.getId() );
		
		assertEquals( 1, watchedHistories.size() );
		assertEquals( testMovie1.getTitle(), watchedHistories.get( 0 ).getMovie().getTitle() );
	}
	@Test
	void shouldDeleteAllHistory() {
		CreateHistoryRequestDTO requestDTO = new CreateHistoryRequestDTO();
		requestDTO.setReact( Reaction.liked );
		requestDTO.setSecondsWatched( 12 );
		
		historyService.addHistory( testUser, requestDTO, testMovie );
		historyService.addHistory( testUser, requestDTO, testMovie1 );
		
		historyService.deleteAll( testUser.getId() );
		
		List<WatchedHistory> watchedHistories = historyService.getHistory( testUser.getId() );
		
		assertEquals( 0, watchedHistories.size() );
	}
}
