package com.upendra.url.service.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IpRateLimitFilter extends OncePerRequestFilter {

	private static final long LIMIT_WINDOW_MS = 5 * 60 * 1000; // 1 minutes
	private final Map<String, Long> ipLastRequestTime = new ConcurrentHashMap<>();

	List<String> rateLimitedEndpoints = List.of("api/v1/");

	Map<String, Duration> newRateLimitedEndpoints = Map.of("api/v1",
			Duration.ofMinutes(2), "api/v1/api/free/other-endpoint", Duration.ofMinutes(1));

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		// Only limit this endpoint
		if (rateLimitedEndpoints.contains(uri)) {
			String ip = request.getRemoteAddr();
			long now = System.currentTimeMillis();
			Long lastRequest = ipLastRequestTime.get(ip);

			if (lastRequest != null && (now - lastRequest) < LIMIT_WINDOW_MS) {
				long wait = (LIMIT_WINDOW_MS - (now - lastRequest)) / 1000;
				response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
				response.setContentType("application/json");
				response.getWriter().write(
						"{ \"code\": 429, \"message\": \"Too many requests at the moment. Please wait WAIT seconds before your next attempt.\" }");
				return; // stop processing
			}
			ipLastRequestTime.put(ip, now);
		}
		filterChain.doFilter(request, response);
	}
}
