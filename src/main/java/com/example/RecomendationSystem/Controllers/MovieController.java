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
import com.example.RecomendationSystem.Service.UserServiceImpl;
@RestController
public class MovieController {
	
	private RecomendationService recomendationService;
	
	private MovieService movieService;
	
	private UserServiceImpl userService;
	@Autowired
	public MovieController(RecomendationService recomendationService,MovieService movieService,
			UserServiceImpl userService) {
		this.movieService = movieService;
		this.recomendationService = recomendationService;
		this.userService = userService;
	}
	@PostMapping("/addMovie")
	public void addMovie(@RequestBody CreateMovieDTO createMovieDTO) {
		//for(CreateMovieDTO c : createMovieDTO) {
		System.out.println( createMovieDTO.getTitle() + " " + createMovieDTO.getTypes() );
		movieService.saveMovie( createMovieDTO );
		//}
	}
	@GetMapping("/getList")
	public List<MovieResponseDTO> showMovieList(){
		User user = userService.getUserById( 1 );
		System.out.println( user.toString() );
		return recomendationService.getRecommend( user );
	}
	@DeleteMapping("/deleteAllMovies")
	public void deleteAll() {
		movieService.deleteAll();
	}
	
}
