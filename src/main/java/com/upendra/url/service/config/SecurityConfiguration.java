package com.upendra.url.service.config;

import static com.upendra.url.service.entities.Role.ADMIN;
import static com.upendra.url.service.enums.Permission.ADMIN_CREATE;
import static com.upendra.url.service.enums.Permission.ADMIN_DELETE;
import static com.upendra.url.service.enums.Permission.ADMIN_READ;
import static com.upendra.url.service.enums.Permission.ADMIN_UPDATE;
import static com.upendra.url.service.enums.Permission.SUPERADMIN_CREATE;
import static com.upendra.url.service.enums.Permission.SUPERADMIN_DELETE;
import static com.upendra.url.service.enums.Permission.SUPERADMIN_READ;
import static com.upendra.url.service.enums.Permission.SUPERADMIN_UPDATE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.upendra.url.service.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

	private static final String[] WHITE_LIST_URL = {"/{code}", "/v1/auth/**", "/v2/api-docs","/v1/api/free/**", "/v3/api-docs","/api/upload/**","/health",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html" };
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	private final AuthenticationProvider authenticationProvider;
	
	private final LogoutHandler logoutHandler;
	
	private final IpRateLimitFilter ipRateLimitFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL).permitAll()
						.requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name())
						.requestMatchers(GET, "/v1/management/**")
						.hasAnyAuthority(ADMIN_READ.name(), SUPERADMIN_READ.name())
						.requestMatchers(POST, "/v1/management/**")
						.hasAnyAuthority(ADMIN_CREATE.name(),SUPERADMIN_CREATE.name())
						.requestMatchers(PUT, "/v1/management/**")
						.hasAnyAuthority(ADMIN_UPDATE.name(), SUPERADMIN_UPDATE.name())
						.requestMatchers(DELETE, "/v1/management/**")
						.hasAnyAuthority(ADMIN_DELETE.name(), SUPERADMIN_DELETE.name()).anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout.logoutUrl("/v1/auth/logout").addLogoutHandler(logoutHandler)
						.logoutSuccessHandler(
								(request, response, authentication) -> SecurityContextHolder.clearContext()));

		return http.build();
	}
}
