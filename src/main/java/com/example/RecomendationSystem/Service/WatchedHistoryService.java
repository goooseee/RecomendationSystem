package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RecomendationSystem.DTO.CreateHistoryResponseDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.UserPreference;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Repository.MovieRepository;
import com.example.RecomendationSystem.Repository.WatchedHistoryRepository;
@Service
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
		return historyRepository.getByUserId( userId );
	}
	public List<WatchedHistory> getHistoryForScorting(long userId) {
		return historyRepository.getByUserIdAndStatus( userId, CountStatus.NotCount );
	}
	public List<Long> getIdsWatchedMovies(long userId){
		return getHistory( userId).stream().map(h -> h.getMovie().getId()).toList();
	}
	@Transactional
	public void addHistory(User user, CreateHistoryResponseDTO responseDTO) {
		Movie movie = movieRepository.getMovieById( responseDTO.getMovieId() ).orElse( null );
		WatchedHistory history = new WatchedHistory();
		history.setDurationSeconds( responseDTO.getSecondsWatched() );
		history.setMovie( movie );
		history.setReact( responseDTO.getReact() );
		history.setTimesWatched( 1 );
		history.setUser( user );
		history.setWhenWatched(LocalDate.now());
		historyRepository.save( history );
		List<UserPreference> userPreferences = preferenceService.getByUserId( user.getId() );
		preferenceService.saveUserPreference( interestScoringService.calculateInterest( user, userPreferences, getHistory( user.getId() ) ) );
	}
	@Transactional
	public List<WatchedHistory> deleteAll(){
		long userId = 1;
		historyRepository.deleteAllByUserId( userId );
		return historyRepository.getByUserId(userId);
	}
	
	public List<WatchedHistory> deleteById(long movieId){
		long userId = 1;
		historyRepository.deleteWatchedHistoryByUserIdAndMovieId( userId,movieId );
		return historyRepository.getByUserId(userId);
	}
}
