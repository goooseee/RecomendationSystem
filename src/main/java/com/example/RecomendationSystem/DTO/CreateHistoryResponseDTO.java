package com.example.RecomendationSystem.DTO;

import com.example.RecomendationSystem.Entity.Enum.Reaction;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class CreateHistoryResponseDTO {
	
	private long movieId;
	
	private long secondsWatched;
	
	private Reaction react;
	
}
