package com.example.RecomendationSystem.DTO;

import com.example.RecomendationSystem.Entity.Enum.Reaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateHistoryDTORequest {
	
	private Long userId;
	private Long movieId;
	private Reaction react;
	private long durationSeconds;
	private int timesWatched;
	
}
