package com.example.RecomendationSystem.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.JwtRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
@Service
public class JwtService {
	@Value("${security.jwt.secret_key}")
	private final String secretKey;
	@Value("${security.jwt.access_token_expiration}")
	private final long accessTokenExpration;
	@Value("${security.jwt.refresh_token_expiration}")
	private final long refreshTokenExpration;
	
	//private final LocalDate dateCookie;
	
	private final int maxAge = 36000000;
    
    private final long longMaxAge = maxAge/86400;
	
    private final JwtRepository jwtRepository;
    
    private final Clock clock;
    
    public JwtService(@Value("${security.jwt.secret_key}") String secretKey,
    		@Value("${security.jwt.access_token_expiration}") long accessTokenExpration,
			@Value("${security.jwt.refresh_token_expiration}") long refreshTokenExpration,
			JwtRepository jwtRepository,
			Clock clock) {
    	this.secretKey = secretKey;
    	this.accessTokenExpration = accessTokenExpration;
    	this.refreshTokenExpration = refreshTokenExpration;
    	this.jwtRepository = jwtRepository;
    	this.clock = clock;
    }
    
    private SecretKey getSigningKey() {
    	byte[] keyBytes = Decoders.BASE64URL.decode( secretKey );
    	
    	return Keys.hmacShaKeyFor( keyBytes );
    }
    
    private String generateToken(User user, long expireTime) {
    	long c = clock.millis();
    	JwtBuilder builder = Jwts.builder()
    			.subject( user.getUsername() )
    			.issuedAt( new Date(c) )
    			.expiration( new Date(c+expireTime) )
    			.signWith( getSigningKey() );
    	return builder.compact();
    }
    
    public String generateAccessToken(User user) {
    	return generateToken( user, accessTokenExpration );
    }
    
    public String generateRefreshToken(User user) {
    	return generateToken( user, refreshTokenExpration );
    }
    
    private Claims extractAllClaims(String token) {
    	return Jwts.parser()
    			.verifyWith( getSigningKey() )
    			.build()
    			.parseSignedClaims( token )
    			.getPayload();
    }
    
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
    	Claims claims = extractAllClaims( token );
    	return resolver.apply( claims );
    }
    
    public String extractUsername(String token) {
		return extractClaims( token, Claims::getSubject);
	}
	
	private Date extractExpiration(String token) {
		return extractClaims( token, Claims::getExpiration );
	}
	
	private boolean isAccessTokenExpired(String token) {
        return !extractExpiration(token).before(new Date(clock.millis()));
    }
	
	public boolean isValidAccess (String token, UserDetails user) {
		
		String username = extractUsername( token );
		
		boolean isValidToken = jwtRepository.findByAccessToken( token )
				.map( t -> !t.isLoggedOut()).orElse( false );
		return username.equals(user.getUsername())
				&& isAccessTokenExpired( token )
				&& isValidToken;
		
	}
	
	public boolean isValidRefresh(String token, User user) {

		String username = extractUsername( token );
		
		boolean isValidToken = jwtRepository.findByRefreshToken( token )
				.map(t -> !t.isLoggedOut()).orElse( false );
		
		return username.equals(user.getUsername())
				&& isAccessTokenExpired( token )
				&& isValidToken;
		
	}
}
