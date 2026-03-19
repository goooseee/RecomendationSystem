package com.example.RecomendationSystem.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.RecomendationSystem.DTO.CreateHistoryResponseDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.UserService;
import com.example.RecomendationSystem.Service.WatchedHistoryService;
@RestController
public class WatchedHistoryController {
	
	private WatchedHistoryService watchedHistoryService;
	
	private MovieService movieService;
	
	private UserService userService;
	@Autowired
	public WatchedHistoryController(WatchedHistoryService watchedHistoryService,MovieService movieService,
			UserService userService) {
		this.watchedHistoryService = watchedHistoryService;
		this.movieService = movieService;
		this.userService = userService;
	}
	@PostMapping("/addHistory")
	public void addHistory(@RequestBody List<CreateHistoryResponseDTO> createHistoryResponseDTO) {
		User user = userService.getUserById( 1 );
		for(CreateHistoryResponseDTO c : createHistoryResponseDTO) {
		watchedHistoryService.addHistory( user, c );
		}
	}
	@GetMapping("/getHistory")
	public List<WatchedHistory> getHistory() {
		long userId = 1;
		return watchedHistoryService.getHistory(userId);
	}
	@DeleteMapping("/deleteAllHistory")
	public List<WatchedHistory> deleteAllHistory(){
		return watchedHistoryService.deleteAll();
	}
	@DeleteMapping("/deleteHistoryById")
	public List<WatchedHistory> deleteHistoryById(){
		return watchedHistoryService.deleteAll();
	}
}
