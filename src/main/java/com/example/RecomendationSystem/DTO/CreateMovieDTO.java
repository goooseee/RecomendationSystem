package com.example.RecomendationSystem.DTO;

import java.util.List;

import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class CreateMovieDTO {
	
	private String title;
	
	private List<Type> types;
	
	private long duration;
	
}
