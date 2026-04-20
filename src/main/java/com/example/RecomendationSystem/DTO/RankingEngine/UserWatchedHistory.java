package com.example.RecomendationSystem.DTO.RankingEngine;

import java.time.LocalDate;
import java.util.List;

import com.example.RecomendationSystem.Entity.Enum.CountStatus;
import com.example.RecomendationSystem.Entity.Enum.Reaction;
import com.example.RecomendationSystem.Entity.Enum.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class UserWatchedHistory {
	private long userId;
	private MovieDTO movie;
	private Reaction react;
	private LocalDate whenWatched;
	private long durationSeconds;
	private int timesWatched;
	private CountStatus status;
}
