package com.example.RecomendationSystem.DTO;

import com.example.RecomendationSystem.Entity.Enum.Reaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class CreateHistoryRequestDTO {
	
	private long movieId;
	
	private long secondsWatched;
	
	private Reaction react;
	
}
