package com.example.RecomendationSystem.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.JwtRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtService {
	
	private String secretKey;
	
	private long accessTokenExpration;
	
	private long refreshTokenExpration;
	
	private LocalDate dateCookie;
	
	private int maxAge = 36000000;
    
    private long longMaxAge = maxAge/86400;
	
    private JwtRepository jwtRepository;
    
    private SecretKey getSigningKey() {
    	byte[] keyBytes = Decoders.BASE64URL.decode( secretKey );
    	
    	return Keys.hmacShaKeyFor( keyBytes );
    }
    
    private String generateToken(User user, long expireTime) {
    	JwtBuilder builder = Jwts.builder()
    			.setSubject( user.getUsername() )
    			.setIssuedAt( new Date(System.currentTimeMillis()) )
    			.setExpiration( new Date(System.currentTimeMillis()+expireTime) )
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
    	JwtParserBuilder parser = Jwts.parserBuilder();
    	parser.setSigningKey( getSigningKey() );
    	
    	return parser.build()
    			.parseClaimsJws( token )
    			.getBody();
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
        return !extractExpiration(token).before(new Date());
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
