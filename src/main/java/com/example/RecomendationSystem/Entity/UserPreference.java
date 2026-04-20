package com.example.RecomendationSystem.Entity;

import java.time.LocalDate;
import java.util.Date;

import com.example.RecomendationSystem.Entity.Enum.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Builder
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","types"})})
@AllArgsConstructor
public class UserPreference {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	@Column(name = "types")
	@Enumerated(EnumType.STRING)
	private Type type;
	
	private double weight;
	
	private LocalDate lastUpdate;
	
	public UserPreference(Type type,double weight) {
		this.type = type;
		this.weight = weight;
	}
	public UserPreference() {}
	
	public UserPreference(Type type,User user,double weight, LocalDate lastUpdate) {
		this.type = type;
		this.weight = weight;
		this.user = user;
		this.lastUpdate = lastUpdate;
	}
}
