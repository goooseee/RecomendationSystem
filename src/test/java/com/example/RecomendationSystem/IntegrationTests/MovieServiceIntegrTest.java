package com.example.RecomendationSystem.IntegrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.Repository.MovieRepository;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.UserPreferenceService;

@DataJpaTest
@ActiveProfiles("test")
public class MovieServiceIntegrTest {
	
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private MovieRepository movieRepository;
	
	private MovieService movieService;
	
	@BeforeEach
	void setUp() {
		movieService = new MovieService( movieRepository );
	}
	@Test
	void shouldSaveMovie() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		
		Movie movie = movieService.saveMovie( createMovieDTO );
		
		assertEquals( "testMovie", movie.getTitle() );
		assertEquals( Type.ACTION, movie.getType().get( 0 ) );
		assertEquals( 100, movie.getDurationOfMovieSeconds() );
	}
	@Test
	void shouldntSaveSameMovie() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		CreateMovieDTO createMovieDTO1 = new CreateMovieDTO( "testMovie", List.of(Type.COMEDY), 100, 8 );
		
		movieService.saveMovie( createMovieDTO );
		
		assertEquals( null, movieService.saveMovie( createMovieDTO1 ) );
	}
	@Test
	void shouldGetMovieById() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		CreateMovieDTO createMovieDTO1 = new CreateMovieDTO( "testMovie1", List.of(Type.COMEDY), 100, 8 );
		
		Movie movie = movieService.saveMovie( createMovieDTO );
		movieService.saveMovie( createMovieDTO1 );
		
		assertEquals( "testMovie", movieService.getById( movie.getId() ).getTitle() );
		assertEquals( Type.ACTION, movieService.getById( movie.getId() ).getType().get( 0 ) );
	}
	@Test
	void shouldGetAllMovies() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		CreateMovieDTO createMovieDTO1 = new CreateMovieDTO( "testMovie1", List.of(Type.COMEDY), 100, 8 );
		
		movieService.saveMovie( createMovieDTO );
		movieService.saveMovie( createMovieDTO1 );
		
		assertEquals( 2, movieService.getAll().size() );
	}
	@Test
	void shouldGetMovieByTitle() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		CreateMovieDTO createMovieDTO1 = new CreateMovieDTO( "testMovie1", List.of(Type.COMEDY), 100, 8 );
		
		Movie movie = movieService.saveMovie( createMovieDTO );
		movieService.saveMovie( createMovieDTO1 );
		
		assertEquals( 8, movieService.getByTitle(movie.getTitle()).getRating() );
		assertEquals( Type.ACTION, movieService.getById( movie.getId() ).getType().get( 0 ) );
	}
	@Test
	void shouldDeleteAllMovies() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		CreateMovieDTO createMovieDTO1 = new CreateMovieDTO( "testMovie1", List.of(Type.COMEDY), 100, 8 );
		
		movieService.saveMovie( createMovieDTO );
		movieService.saveMovie( createMovieDTO1 );
		movieService.deleteAll();
		
		assertEquals( 0, movieService.getAll().size() );
	}
	@Test
	void shouldGetUnWatchedMovie() {
		CreateMovieDTO createMovieDTO = new CreateMovieDTO( "testMovie", List.of(Type.ACTION), 100, 8 );
		CreateMovieDTO createMovieDTO1 = new CreateMovieDTO( "testMovie1", List.of(Type.COMEDY), 100, 8 );
		
		Movie movie = movieService.saveMovie( createMovieDTO );
		movieService.saveMovie( createMovieDTO1 );
		
		List<Movie> movies = movieService.getAllUnWatched( List.of(movie.getId()) );
		
		assertEquals( 1, movies.size() );
		assertEquals( "testMovie1", movies.get( 0 ).getTitle() );
		assertEquals( Type.COMEDY, movies.get( 0 ).getType().get( 0 ) );
	}
}
