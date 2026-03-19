package com.example.RecomendationSystem.Service;

import java.time.Duration;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.MovieRepository;
import com.example.RecomendationSystem.Repository.WatchedHistoryRepository;
@Service
public class MovieService {
	
	private MovieRepository movieRepository;
	
	private WatchedHistoryService historyService;
	
	public MovieService(MovieRepository movieRepository,WatchedHistoryService historyService) {
		this.movieRepository = movieRepository;
		this.historyService = historyService;
	}
	
	public Movie getById(long id) {
		return movieRepository.getMovieById( id ).orElse( null );
	}
	
	public Movie getByTitle(String title) {
		return movieRepository.getMovieByTitle( title ).orElse( null );
	}
	
	public Movie saveMovie(CreateMovieDTO createMovieDTO) {
		Movie movie = new Movie(createMovieDTO.getTitle(),createMovieDTO.getTypes(),
				 createMovieDTO.getDuration() );
		System.out.println( createMovieDTO.getTitle() +" "+ createMovieDTO.getTypes() );
		return movieRepository.save( movie );
	}
	public List<Movie> getAll(){
		return movieRepository.findAll();
	}
	
	public List<Movie> getAllUnWatched(List<Long> ids){
		if(ids.isEmpty()) {
			return movieRepository.findTop100OrderByRatingDesc();
		}
		return movieRepository.findTop100ByIdNotInOrderByRatingDesc( ids );
	}
	
	public void deleteAll() {
		movieRepository.deleteAll();
	}
}
