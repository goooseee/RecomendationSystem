package com.example.RecomendationSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.RecomendationSystem.Entity.Token;

public interface JwtRepository extends JpaRepository<Token, Long>{
	
	Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String refreshToken);
	
    @Query("""
            SELECT t FROM Token t inner join User u
            on t.user.id = u.id
            where t.user.id = :userId and t.loggedOut = false
            """)

    List<Token> findAllAccessTokenByUser(@Param("userId") Long userId);
	
}
