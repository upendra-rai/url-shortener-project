package com.upendra.url.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upendra.url.service.repositories.UserRepository;

@Service
public class AuthDetailsProvider {

	@Autowired
	private UserRepository userRepository;

	public static String getLoggedEmail() {
		return JwtAuthenticationFilter.CURRENT_USER;
	}

	public Object getLoggedUserDetails() {
		return userRepository.findByEmail(JwtAuthenticationFilter.CURRENT_USER);
	}

}
