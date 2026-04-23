package com.example.RecomendationSystem.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.RecomendationSystem.Clients.ScoringClient;
import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.DTO.ResponseDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Mapper.ResponseDTOtoMovie;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.UserService;
import com.example.RecomendationSystem.Service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
public class MovieController {
	
	//private final RecomendationService recomendationService;
	
	private final MovieService movieService;
	
	private final UserService userService;
	
	private final ScoringClient scoringClient;
	
	private final ResponseDTOtoMovie responseDTOtoMovie;
	
	@PostMapping("/addMovie")
	public void addMovie(@RequestBody CreateMovieDTO createMovieDTO) {
		movieService.saveMovie( createMovieDTO );
	}
	@GetMapping("/getList")
	public List<Movie> showMovieList(){
		//User user = userService.getUserFromContext();
		ResponseDTO dto = scoringClient.getRecommend( 1 );
		return responseDTOtoMovie.mapToMovie( dto );
	}
	@DeleteMapping("/deleteAllMovies")
	public void deleteAll() {
		movieService.deleteAll();
	}
	
}
