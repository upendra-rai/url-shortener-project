package com.upendra.url.service.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upendra.url.service.dtos.AuthenticationRequest;
import com.upendra.url.service.dtos.RegisterRequest;
import com.upendra.url.service.dtos.UserAuthenticationResponse;
import com.upendra.url.service.services.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
	private AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> customerRegister(@Valid @RequestBody RegisterRequest request) {
		log.info("REST request to register user with email: {}", request.getEmail());
		Map<String, Object> response = service.teacherRegister(request);
		log.info("User registered successfully for email: {}", request.getEmail());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<UserAuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
		log.info("REST request to authenticate user with email: {}", request.getEmail());
		UserAuthenticationResponse response = service.authenticate(request);
		log.info("User authenticated successfully for email: {}", request.getEmail());
		return ResponseEntity.ok(response);
	}

}
