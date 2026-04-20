package com.example.RecomendationSystem.MockTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.Enum.Type;
import com.example.RecomendationSystem.Exception.MovieNotFoundException;
import com.example.RecomendationSystem.Repository.MovieRepository;
import com.example.RecomendationSystem.Service.MovieService;
@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
	@Mock
	private MovieRepository movieRepository;
	@InjectMocks
	MovieService movieService;
	@Test
	@DisplayName("В базе данных не найден фильм с id и было выброшено исключение")
	void DontGetId() {
		long id = 1;
		when( movieRepository.findById( id ) ).thenReturn( Optional.empty() );
		assertThrows(MovieNotFoundException.class,()->{movieService.getById( id );} );
	}
	@Test
	@DisplayName("В базе данных не найден фильм с title и было выброшено исключение")
	void DontGetByTitle() {
		String title = "A";
		when( movieRepository.getMovieByTitle( title ) ).thenReturn( Optional.empty() );
		assertThrows(MovieNotFoundException.class,()->{movieService.getByTitle( title );} );
	}
	@Test
	@DisplayName("При попытке сохранения фильма в базу данных был найден фильм с title, фильм не сохранен")
	void CantSaveMovie() {
		String title = "A";
		CreateMovieDTO createMovieDTO = new CreateMovieDTO();
		createMovieDTO.setTitle( title );
		when(movieRepository.getMovieByTitle( title )).thenReturn( Optional.of(new Movie()) );
		Movie movie = movieService.saveMovie( createMovieDTO );
		assertEquals( null, movie );
		verify( movieRepository, never()).save( any() );
	}
	@Test
	@DisplayName("У нового пользователя не было найдено просмотренных фильмов, получение 100 фильмов основанных на рейтинге")
	void getTop100MoviesInsteandAllUnWatched() {
		Movie movie = new Movie();
		movie.setTitle( "As" );
		movie.setDurationOfMovieSeconds( 1000 );
		movie.setType( List.of(Type.ACTION) );
		when(movieRepository.findTop100ByOrderByRatingDesc()).thenReturn( List.of(movie) );
		List<Long> ids = new ArrayList<>();
		List<Movie> m = movieService.getAllUnWatched( ids );
		assertEquals( List.of(movie), m );
	}
}
