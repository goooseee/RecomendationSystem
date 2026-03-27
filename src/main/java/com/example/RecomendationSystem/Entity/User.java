package com.example.RecomendationSystem.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<WatchedHistory> history;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserPreference> preferences;
	
}
