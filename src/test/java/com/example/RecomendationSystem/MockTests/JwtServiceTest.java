package com.example.RecomendationSystem.MockTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.RecomendationSystem.Entity.Token;
import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Repository.JwtRepository;
import com.example.RecomendationSystem.Service.JwtService;
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
	
	private String secretKey = "4eeab38d706831be4b36612ead768ef8182d1dd6f0e14e5dc934652e297fb16a";

	private long accessTokenExpration = 36000000;

	private long refreshTokenExpration = 252000000;
	
	private JwtRepository jwtRepository = Mockito.mock( JwtRepository.class );
	
	private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());;
	
	private JwtService jwtService = new JwtService(secretKey,accessTokenExpration,
			refreshTokenExpration, jwtRepository, clock);
	@Test
	void shouldCreateAndValidAccessToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateAccessToken( user );
		when( jwtRepository.findByAccessToken( anyString())).thenReturn( Optional.of( new Token() ) );
		assertTrue(jwtService.isValidAccess( token, user ));
		assertEquals( "test", jwtService.extractUsername( token ) );
	}
	@Test
	void shouldCreateAndValidRefreshToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateRefreshToken( user );
		when( jwtRepository.findByRefreshToken( anyString())).thenReturn( Optional.of( new Token() ) );
		assertTrue(jwtService.isValidRefresh( token, user ));
	}
	@Test
	void shouldCreateAndNotFoundAccessToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateAccessToken( user );
		when( jwtRepository.findByAccessToken( anyString())).thenReturn( Optional.empty() );
		assertFalse(jwtService.isValidAccess( token, user ));
	}
	@Test
	void shouldCreateAndNotFoundRefreshToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateRefreshToken( user );
		when( jwtRepository.findByRefreshToken( anyString())).thenReturn( Optional.empty() );
		assertFalse(jwtService.isValidRefresh( token, user ));
	}
	@Test
	void shouldCreateAndUsernameNotEqualAccessToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		User user1 = new User();
		user1.setId( 2L );
		user1.setUsername( "test1" );
		
		String token = jwtService.generateAccessToken( user );
		when( jwtRepository.findByAccessToken( anyString())).thenReturn( Optional.of( new Token() ) );
		assertFalse(jwtService.isValidAccess( token, user1 ));
		assertNotEquals( "test1", jwtService.extractUsername( token ) );
	}
	@Test
	void shouldCreateAndUsernameNotEqualRefreshToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		User user1 = new User();
		user1.setId( 2L );
		user1.setUsername( "test1" );
		
		String token = jwtService.generateRefreshToken( user );
		when( jwtRepository.findByRefreshToken( anyString())).thenReturn( Optional.of( new Token() ) );
		assertFalse(jwtService.isValidRefresh( token, user1 ));
	}
	@Test
	void shouldCreateAndExpiredAccessToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateAccessToken( user );
		
		Instant futureInstant = Instant.parse("2099-04-11T21:00:00Z");
		Clock futureClock = Clock.fixed(futureInstant, ZoneId.of("UTC"));
		ReflectionTestUtils.setField(jwtService, "clock", futureClock);
		
		assertFalse(jwtService.isValidAccess( token, user ));
	}
	@Test
	void shouldCreateAndExpiredRefreshToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateRefreshToken( user );
		
		Instant futureInstant = Instant.parse("2099-04-11T21:00:00Z");
		Clock futureClock = Clock.fixed(futureInstant, ZoneId.of("UTC"));
		ReflectionTestUtils.setField(jwtService, "clock", futureClock);
		
		assertFalse(jwtService.isValidRefresh( token, user ));
	}
	@Test
	void shouldLoggedOutAccessToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateAccessToken( user );
		Token tokendb = new Token();
		tokendb.setLoggedOut( true );
		
		when(jwtRepository.findByAccessToken( anyString() )).thenReturn( Optional.of( tokendb ) );
		
		assertFalse(jwtService.isValidAccess( token, user ));
	}
	@Test
	void shouldLoggedInAccessToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateAccessToken( user );
		Token tokendb = new Token();
		tokendb.setLoggedOut( false );
		
		when(jwtRepository.findByAccessToken( anyString() )).thenReturn( Optional.of( tokendb ) );
		
		assertTrue(jwtService.isValidAccess( token, user ));
	}
	@Test
	void shouldLoggedOutRefreshToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateRefreshToken( user );
		Token tokendb = new Token();
		tokendb.setLoggedOut( true );
		
		when(jwtRepository.findByRefreshToken( anyString() )).thenReturn( Optional.of( tokendb ) );
		
		assertFalse(jwtService.isValidRefresh( token, user ));
	}
	@Test
	void shouldLoggedInRefreshToken() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "test" );
		
		String token = jwtService.generateRefreshToken( user );
		Token tokendb = new Token();
		tokendb.setLoggedOut( false );
		
		when(jwtRepository.findByRefreshToken( anyString() )).thenReturn( Optional.of( tokendb ) );
		
		assertTrue(jwtService.isValidRefresh( token, user ));
	}
}
