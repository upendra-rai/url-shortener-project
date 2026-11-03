package com.upendra.url.service.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upendra.url.service.dtos.ApiResponseData;
import com.upendra.url.service.dtos.AuthenticationRequest;
import com.upendra.url.service.dtos.ForgotPasswordRequest;
import com.upendra.url.service.dtos.RegisterRequest;
import com.upendra.url.service.dtos.UserAuthenticationResponse;
import com.upendra.url.service.dtos.VerificationRequest;
import com.upendra.url.service.security.AuthDetailsProvider;
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
		return ResponseEntity.ok(service.teacherRegister(request));
	}

	@PostMapping("/login")
	public ResponseEntity<UserAuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}


}
