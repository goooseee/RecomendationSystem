package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Exception.MovieNotFoundException;
import com.example.RecomendationSystem.Repository.MovieRepository;
import com.example.RecomendationSystem.Repository.WatchedHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {
	
	private final MovieRepository movieRepository;
	
	public Movie getById(long id) {
		log.atDebug().log( "Getting movie with id = {}",id );
		return movieRepository.findById( id )
				.orElseThrow(() -> new MovieNotFoundException( "Movie not found with id: " + id ) );
	}
	
	public Movie getByTitle(String title) {
		log.atDebug().log( "Getting movie with title = {}",title );
		return movieRepository.getMovieByTitle( title )
				.orElseThrow(() -> new MovieNotFoundException( "Movie not found with title: " + title ) );
	}
	
	public Movie saveMovie(CreateMovieDTO createMovieDTO) {
		log.atInfo().log( "Trying to save movie with request = {}",createMovieDTO.toString() );
		
		if(movieRepository.getMovieByTitle( createMovieDTO.getTitle() ).isPresent()) {
			return null;
		}
		
		Movie movie = Movie.builder()
				.title( createMovieDTO.getTitle() )
				.durationOfMovieSeconds( createMovieDTO.getDuration() )
				.type(createMovieDTO.getTypes())
				.rating(createMovieDTO.getRating())
				.build();
		log.atInfo().log("Saving movie = {}", movie.toString());
		return movieRepository.save( movie );
	}
	public List<Movie> getAll(){
		log.atDebug().log( "Getting all movies" );
		return movieRepository.findAll();
	}
	
	public List<Movie> getAllUnWatched(List<Long> ids){
		log.atDebug().log( "Getting all unwatched movies" );
		if(ids.isEmpty()) {
			log.atDebug().log( "Getting Top 100 movies because ids was empty" );
			return movieRepository.findTop100ByOrderByRatingDesc();
		}
		return movieRepository.findTop100ByIdNotInOrderByRatingDesc( ids );
	}
	
	public void deleteAll() {
		log.atWarn().log( "Every movie will be delete" );
		movieRepository.deleteAll();
	}
}
