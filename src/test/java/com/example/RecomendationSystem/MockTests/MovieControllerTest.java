package com.example.RecomendationSystem.MockTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; 

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.RecomendationSystem.Controllers.MovieController;
import com.example.RecomendationSystem.DTO.CreateMovieDTO;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Service.JwtService;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.RecomendationService;
import com.example.RecomendationSystem.Service.UserService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(value = MovieController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class MovieControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	@MockitoBean
	private RecomendationService recomendationService;
	@MockitoBean
	private MovieService movieService;
	@MockitoBean
	private UserService userService;
	@Autowired
	private ObjectMapper objectMapper;
	@MockitoBean
	private JwtService jwtService;
	@Test
	void testGetList() throws Exception{
		User user = new User();
		when(userService.getUserFromContext()).thenReturn( user );
		when(recomendationService.getRecommend( user )).thenReturn( List.of() );
		
		mockMvc.perform( get("/getList") )
						.andExpect( status().isOk() )
						.andExpect( content().contentType( MediaType.APPLICATION_JSON ) );
	}
	@Test
	void testAddMovie() throws Exception{
		CreateMovieDTO dto = new CreateMovieDTO();
		
		mockMvc.perform( post("/addMovie") 
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
						.andExpect( status().isOk() );
		verify( movieService, times( 1 ) ).saveMovie( any(CreateMovieDTO.class) );
	}
	@Test
	void testDeleteAll() throws Exception{
		mockMvc.perform( delete("/deleteAllMovies") )
						.andExpect( status().isOk() );
		verify( movieService, times(1) ).deleteAll();
	}
}
