package com.example.RecomendationSystem.Config;

import java.io.IOException;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if("XMLHttpRequest".equals( request.getHeader( "X-Requested-With" ) )) {
			response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
		}else {
			response.sendRedirect( "/login" );
		}
		
	}

}
