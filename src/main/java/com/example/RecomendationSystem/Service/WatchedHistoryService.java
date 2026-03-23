package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Repository.MovieRepository;
import com.example.RecomendationSystem.Repository.WatchedHistoryRepository;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class WatchedHistoryService {
	
	private WatchedHistoryRepository historyRepository;
	
	private MovieRepository movieRepository;
	
	private UserPreferenceService preferenceService;
	
	private InterestScoringService interestScoringService;
	
	public WatchedHistoryService (MovieRepository movieRepository,WatchedHistoryRepository historyRepository,
			UserPreferenceService preferenceService,InterestScoringService interestScoringService) {
		this.movieRepository = movieRepository;
		this.historyRepository = historyRepository;
		this.preferenceService = preferenceService;
		this.interestScoringService = interestScoringService;
	}
	
	public List<WatchedHistory> getHistory(long userId) {
		log.atDebug().log( "Getting all history user with id = {}",userId );
		return historyRepository.getByUserId( userId );
	}
	public List<WatchedHistory> getHistoryForScorting(long userId) {
		log.atDebug().log( "Getting history user with id = {} for scoring",userId );
		return historyRepository.getByUserIdAndStatus( userId, CountStatus.NotCount );
	}
	public List<Long> getIdsWatchedMovies(long userId){
		log.atDebug().log( "Getting ids of movies that user watched with id = {}",userId );
		return getHistory( userId).stream().map(h -> h.getMovie().getId()).toList();
	}
	@Transactional
	public void addHistory(User user, CreateHistoryRequestDTO requestDTO) {
		log.atDebug().log( "Create history with response = {} for user = {}",requestDTO.toString(),user.getUsername() );
		Movie movie = movieRepository.getMovieById( requestDTO.getMovieId() ).orElse( null );
		WatchedHistory history = new WatchedHistory();
		history.setDurationSeconds( requestDTO.getSecondsWatched() );
		history.setMovie( movie );
		history.setReact( requestDTO.getReact() );
		history.setTimesWatched( 1 );
		history.setUser( user );
		history.setWhenWatched(LocalDate.now());
		historyRepository.save( history );
		log.atDebug().log( "Saving history" );
		List<UserPreference> userPreferences = preferenceService.getByUserId( user.getId() );
		preferenceService.saveUserPreference( interestScoringService.calculateInterest( user, userPreferences, getHistory( user.getId() ) ) );
		log.atDebug().log("Preference user = {} was update", user.getUsername());
	}
	@Transactional
	public List<WatchedHistory> deleteAll(){
		long userId = 1;
		log.atDebug().log("Deleting all history for user = {}",userId);
		historyRepository.deleteAllByUserId( userId );
		return historyRepository.getByUserId(userId);
	}
	
	public List<WatchedHistory> deleteById(long movieId){
		long userId = 1;
		log.atDebug().log("Deleting history = {} for user = {}",movieId,userId);
		historyRepository.deleteWatchedHistoryByUserIdAndMovieId( userId,movieId );
		return historyRepository.getByUserId(userId);
	}
}
