package com.example.RecomendationSystem.MockTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.RecomendationSystem.Controllers.MovieController;
import com.example.RecomendationSystem.Controllers.WatchedHistoryController;
import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Service.JwtService;
import com.example.RecomendationSystem.Service.UserActivityOrchestrator;
import com.example.RecomendationSystem.Service.UserService;
import com.example.RecomendationSystem.Service.WatchedHistoryService;

import tools.jackson.databind.ObjectMapper;
@WebMvcTest(value = WatchedHistoryController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class WatchedHistoryServiceTest {
	@Autowired
    private MockMvc mockMvc;
	@MockitoBean
	private UserActivityOrchestrator orchestrator; 
	@MockitoBean
	private WatchedHistoryService watchedHistoryService;
	@MockitoBean
	private UserService userService;
	@MockitoBean
	private JwtService jwtService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void testGetList() throws Exception{
		long id = 1;
		when(userService.getUserIdFromContext()).thenReturn( id );
		when(watchedHistoryService.getHistory( id )).thenReturn( List.of() );
		
		mockMvc.perform( get("/getHistory") )
						.andExpect( status().isOk() )
						.andExpect( content().contentType( MediaType.APPLICATION_JSON ) );
	}
	@Test
	void testDeleteAllHistory() throws Exception{
		long id = 1;
		when(userService.getUserIdFromContext()).thenReturn( id );
		when(watchedHistoryService.deleteAll( id )).thenReturn( List.of() );
		
		mockMvc.perform( delete("/deleteAllHistory") )
						.andExpect( status().isOk() );
		verify( watchedHistoryService, times(1) ).deleteAll( id );
	}
	@Test
	void testDeleteHistoryById() throws Exception{
		long id = 1;
		long q = 1;
		when(userService.getUserIdFromContext()).thenReturn( id );
		when(watchedHistoryService.deleteById( id,q )).thenReturn( List.of() );
		
		mockMvc.perform( delete("/deleteHistoryById")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(q)) )
						.andExpect( status().isOk() );
						//.andExpect( content().contentType( MediaType.APPLICATION_JSON ) );
		verify( watchedHistoryService, times( 1 ) ).deleteById( id, q );
	}
	@Test
	void testAddHistory() throws Exception{
	    CreateHistoryRequestDTO dto1 = new CreateHistoryRequestDTO();
	    dto1.setMovieId( 123 );
	    CreateHistoryRequestDTO dto2 = new CreateHistoryRequestDTO();
	    dto2.setMovieId( 1223 );
	    List<CreateHistoryRequestDTO> dtoList = List.of(dto1, dto2);
		
		mockMvc.perform( post("/addHistory") 
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dtoList)) )
						.andExpect( status().isOk() );
		verify( orchestrator, times(2) ).whenWatchedMovie( any(CreateHistoryRequestDTO.class) );
	}
}
