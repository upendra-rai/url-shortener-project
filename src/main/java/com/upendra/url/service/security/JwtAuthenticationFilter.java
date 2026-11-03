package com.upendra.url.service.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private  JwtService jwtService;

	@Autowired
	private  UserDetailsService userDetailsService;

	public static String CURRENT_USER = ""; 
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if (servletPath.startsWith("/v1/auth") || servletPath.startsWith("/api/v1/auth")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUsername(jwt);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			//var isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked())
					//.orElse(false);
			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				//Getting Current User Email Address
				CURRENT_USER=userDetails.getUsername();
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				log.debug("Authenticated user '{}' for path: {}", userEmail, request.getServletPath());
			}
		}
		filterChain.doFilter(request, response);
	}
}
