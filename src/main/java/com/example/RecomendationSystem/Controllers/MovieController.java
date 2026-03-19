package com.example.RecomendationSystem.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.DTO.MovieResponseDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.RecomendationService;
import com.example.RecomendationSystem.Service.UserService;
@RestController
public class MovieController {
	
	private RecomendationService recomendationService;
	
	private MovieService movieService;
	
	private UserService userService;
	@Autowired
	public MovieController(RecomendationService recomendationService,MovieService movieService,
			UserService userService) {
		this.movieService = movieService;
		this.recomendationService = recomendationService;
		this.userService = userService;
	}
	@PostMapping("/addMovie")
	public void addMovie(@RequestBody List<CreateMovieDTO> createMovieDTO) {
		for(CreateMovieDTO c : createMovieDTO) {
		System.out.println( c.getTitle() + " " + c.getTypes() );
		movieService.saveMovie( c );
		}
	}
	@GetMapping("/getList")
	public List<MovieResponseDTO> showMovieList(){
		User user = userService.getUserById( 1 );
		return recomendationService.createRecomendation( user );
	}
	@DeleteMapping("/deleteAllMovies")
	public void deleteAll() {
		movieService.deleteAll();
	}
	
}
