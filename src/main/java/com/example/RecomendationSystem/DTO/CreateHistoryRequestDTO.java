package com.example.RecomendationSystem.DTO;

import com.example.RecomendationSystem.Entity.Enum.Reaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateHistoryRequestDTO {
	
	private long movieId;
	
	private long secondsWatched;
	
	private Reaction react;
	
}
