package com.example.RecomendationSystem.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Repository.WatchedHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class WatchedHistoryService {
	
	private final WatchedHistoryRepository historyRepository;
	
	public List<WatchedHistory> getHistory(long userId) {
		log.atDebug().log( "Getting all history user with id = {}",userId );
		return historyRepository.getByUserId( userId );
	}
	public List<WatchedHistory> getHistoryForScorting(long userId) {
		log.atDebug().log( "Getting history user with id = {} for scoring",userId );
		return historyRepository.getByUserIdAndStatus( userId, List.of(CountStatus.NotCount) );
	}
	public List<Long> getIdsWatchedMovies(long userId){
		log.atDebug().log( "Getting ids of movies that user watched with id = {}",userId );
		return historyRepository.getByUserIdAndStatus( userId, List.of(CountStatus.NotCount, CountStatus.Count) )
				.stream().map(h -> h.getMovie().getId()).toList();
	}
	
	public void saveAll(List<WatchedHistory> a) {
		historyRepository.saveAll( a );
	}
	
	public void addHistory(User user, CreateHistoryRequestDTO requestDTO, Movie movie) {
		
		WatchedHistory history = WatchedHistory.builder()
				.durationSeconds(requestDTO.getSecondsWatched())
				.movie( movie )
				.react( requestDTO.getReact() )
				.status( CountStatus.NotCount )
				.user( user )
				.timesWatched( 1 )
				.whenWatched( LocalDate.now() )
				.build();
				
		historyRepository.save( history );
		log.atDebug().log( "Saving history" );
	}
	@Transactional
	public List<WatchedHistory> deleteAll(long userId){
		log.atDebug().log("Deleting all history for user = {}",userId);
		historyRepository.deleteAllByUserId( userId );
		return historyRepository.getByUserId(userId);
	}
	@Transactional
	public List<WatchedHistory> deleteById(long movieId, long userId){
		log.atDebug().log("Deleting history = {} for user = {}",movieId,userId);
		historyRepository.deleteWatchedHistoryByUserIdAndMovieId( userId,movieId );
		return historyRepository.getByUserId(userId);
	}
}
