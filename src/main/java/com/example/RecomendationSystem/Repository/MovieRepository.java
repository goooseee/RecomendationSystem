package com.example.RecomendationSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.WatchedHistory;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
	
	Optional<Movie> getMovieById(long id);
	
	Optional<Movie> getMovieByTitle(String title);
	
	public List<Movie> findTop100ByIdNotInOrderByRatingDesc(List<Long> ids);
	
	public List<Movie> findTop100OrderByRatingDesc();
}
