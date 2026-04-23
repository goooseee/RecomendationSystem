package com.example.RecomendationSystem.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.DTO.ResponseDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Service.MovieService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseDTOtoMovie {
	
	private final MovieService movieService;
	
	public List<Movie> mapToMovie(ResponseDTO dto){
		List<Movie> movies = new ArrayList<>();
		List<Long> ids = dto.getMoviesId();
		for(Long id : ids) {
			movies.add( movieService.getById( id ) );
		}
		return movies;
	}
	
}
