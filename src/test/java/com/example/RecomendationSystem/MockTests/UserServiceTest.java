package com.example.RecomendationSystem.MockTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Exception.UserNotFoundException;
import com.example.RecomendationSystem.Repository.UserRepository;
import com.example.RecomendationSystem.Service.UserServiceImpl;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	void ShouldAdd(){
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		when(userRepository.save( user )).thenReturn( user );
		
		User user1 = userServiceImpl.addUser( user );
		assertEquals( user.getUsername(), user1.getUsername() );
	}
	@Test
	void ShouldGetById(){
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		when(userRepository.findById( user.getId() )).thenReturn( Optional.of(user) );
		
		User user1 = userServiceImpl.getUserById( user.getId() );
		assertEquals( user.getUsername(), user1.getUsername() );
	}
	@Test
	void ShouldntGetById(){
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		when(userRepository.findById( user.getId()+1 )).thenReturn( Optional.empty() );
		
		assertThrows(UserNotFoundException.class,() -> userServiceImpl.getUserById( user.getId()+1 ) );
	}
	@Test
	void ShouldFindByUsername(){
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		when(userRepository.existsByUsername( user.getUsername() )).thenReturn( true );
		
		assertTrue( userServiceImpl.existByUsername( "testUser" ) );
	}
	@Test
	void ShouldLoadByUsername(){
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		when(userRepository.findByUsername( user.getUsername() )).thenReturn( Optional.of(user) );
		
		User user1 = (User) userServiceImpl.loadUserByUsername( "testUser" );
		assertNotNull( user1 );
	}
	@Test
	void ShouldntLoadByUsername(){
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		when(userRepository.findByUsername( "testUser1" )).thenReturn( Optional.empty() );
		
		assertThrows(UserNotFoundException.class,() -> userServiceImpl.loadUserByUsername( "testUser1" ) );
	}
	@Test
	void shouldGetUserFromContext() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		Authentication auth = mock(Authentication.class);
		SecurityContext context = mock(SecurityContext.class);
		
		when(auth.getPrincipal()).thenReturn( user );
		when(context.getAuthentication()).thenReturn( auth );
		
		try(MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)){
			mockedContext.when( SecurityContextHolder::getContext ).thenReturn( context );
			
			User resUser = userServiceImpl.getUserFromContext();
			assertNotNull( resUser );
			assertEquals( 1L, resUser.getId() );
		}
	}
	@Test
	void shouldGetUsernameFromContext() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		Authentication auth = mock(Authentication.class);
		
		when(auth.getPrincipal()).thenReturn( "anonymousUser" );
		when(auth.getName()).thenReturn( user.getUsername() );
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		when(userRepository.findByUsername( user.getUsername() )).thenReturn( Optional.of(user) );
		
		User user1 = userServiceImpl.getUserFromContext();
		
		assertEquals( user.getUsername(), user1.getUsername() );
		
		SecurityContextHolder.clearContext();
	}
	@Test
	void shouldGetUserIdFromContext() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		Authentication auth = mock(Authentication.class);
		SecurityContext context = mock(SecurityContext.class);
		
		when(auth.getPrincipal()).thenReturn( user );
		when(context.getAuthentication()).thenReturn( auth );
		
		try(MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)){
			mockedContext.when( SecurityContextHolder::getContext ).thenReturn( context );
			
			Long id = userServiceImpl.getUserIdFromContext();
			assertNotNull( id );
			assertEquals( 1L, id );
		}
	}
	@Test
	void shouldReturnNullIdWhenNoUserInContext() {
		User user = new User();
		user.setId( 1L );
		user.setUsername( "testUser" );
		Authentication auth = mock(Authentication.class);
		SecurityContext context = mock(SecurityContext.class);
		
		when(auth.getPrincipal()).thenReturn( new User() );
		when(context.getAuthentication()).thenReturn( auth );
		
		try(MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)){
			mockedContext.when( SecurityContextHolder::getContext ).thenReturn( context );
			
			Long id = userServiceImpl.getUserIdFromContext();
			assertNull( id );
		}
	}
}
