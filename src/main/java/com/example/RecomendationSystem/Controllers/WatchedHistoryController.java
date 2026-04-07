package com.example.RecomendationSystem.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.RecomendationSystem.DTO.CreateHistoryRequestDTO;
import com.example.RecomendationSystem.Entity.Movie;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Service.MovieService;
import com.example.RecomendationSystem.Service.UserActivityOrchestrator;
import com.example.RecomendationSystem.Service.UserService;
import com.example.RecomendationSystem.Service.UserServiceImpl;
import com.example.RecomendationSystem.Service.WatchedHistoryService;

import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
public class WatchedHistoryController {
	
	private final UserActivityOrchestrator orchestrator; 
	
	private final WatchedHistoryService watchedHistoryService;
	
	private final UserService userService;
	
	@PostMapping("/addHistory")
	@Transactional
	public void addHistory(@RequestBody List<CreateHistoryRequestDTO> createHistoryRequestDTO) {
		for(CreateHistoryRequestDTO c : createHistoryRequestDTO) {
		orchestrator.whenWatchedMovie( c );
		}
	}
	@GetMapping("/getHistory")
	public List<WatchedHistory> getHistory() {
		long userId = userService.getUserIdFromContext();
		return watchedHistoryService.getHistory(userId);
	}
	@DeleteMapping("/deleteAllHistory")
	public List<WatchedHistory> deleteAllHistory(){
		long userId = userService.getUserIdFromContext();
		return watchedHistoryService.deleteAll(userId);
	}
	@DeleteMapping("/deleteHistoryById")
	public List<WatchedHistory> deleteHistoryById(){
		long userId = userService.getUserIdFromContext();
		return watchedHistoryService.deleteAll(userId);
	}
}
