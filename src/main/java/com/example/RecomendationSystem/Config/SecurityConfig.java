package com.example.RecomendationSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.example.RecomendationSystem.Filter.JwtFilter;
import com.example.RecomendationSystem.Handler.CustomAccessDeniedHandler;
import com.example.RecomendationSystem.Handler.CustomLogoutHandler;
import com.example.RecomendationSystem.Service.UserService;

import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final UserService userService;
	
	private final JwtFilter jwtFilter;
	
	private final CustomAccessDeniedHandler accessDeniedHandler;

    private final CustomLogoutHandler customLogoutHandler;
    
    private final CustomEntryPoint customEntryPoint;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) {
		
		return http
				.authorizeHttpRequests( n -> 
						n.requestMatchers("/login","/registration/**", "/css/**", "/refresh_token", "/")
		    			.permitAll().anyRequest().permitAll() )
				.userDetailsService( userService )
				.exceptionHandling( e -> {
					e.accessDeniedHandler( accessDeniedHandler );
					e.authenticationEntryPoint( customEntryPoint );
				})
				.sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
				.addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class )
				.logout( log -> {
					log.logoutUrl( "/logout" );
					log.addLogoutHandler( customLogoutHandler );
					log.logoutSuccessHandler( (request, response, authentication) -> SecurityContextHolder.clearContext() );
				} )
		.build();
		
	}
	
}
