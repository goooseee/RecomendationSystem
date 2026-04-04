package com.example.RecomendationSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.WatchedHistory;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{
	
	Optional<Movie> getMovieById(long id);
	
	Optional<Movie> getMovieByTitle(String title);
	@EntityGraph(attributePaths = {"type"})
	public List<Movie> findTop100ByIdNotInOrderByRatingDesc(List<Long> id);
	@EntityGraph(attributePaths = {"type"})
	public List<Movie> findTop100ByOrderByRatingDesc();
	@Query("""
			SELECT m from Movie m
			where m.id not in (
			select h.movie.id from WatchedHistory h where h.user.id=:user_id
			)
			ORDER BY m.rating DESC
			LIMIT 100
			""")
	public List<Movie> getTopCandidte(@Param("user_id") Long userId);
	
	
}
