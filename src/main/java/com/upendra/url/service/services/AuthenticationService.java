package com.upendra.url.service.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upendra.url.service.dtos.AuthenticationRequest;
import com.upendra.url.service.dtos.AuthenticationResponse;
import com.upendra.url.service.dtos.LoggedInUserDetails;
import com.upendra.url.service.dtos.MailRequest;
import com.upendra.url.service.dtos.RegisterRequest;
import com.upendra.url.service.dtos.UserAuthenticationResponse;
import com.upendra.url.service.entities.Role;
import com.upendra.url.service.entities.Token;
import com.upendra.url.service.entities.User;
import com.upendra.url.service.enums.TokenType;
import com.upendra.url.service.enums.UserStatus;
import com.upendra.url.service.exceptions.ServiceException;
import com.upendra.url.service.repositories.TokenRepository;
import com.upendra.url.service.repositories.UserRepository;
import com.upendra.url.service.security.JwtService;
import com.upendra.url.service.util.RandomUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ModelMapper mapper;

	// Customer Register
	@Transactional
	public Map<String, Object> teacherRegister(RegisterRequest request) {
		validateDuplicateUser(request.getEmail(), request.getPhone());
		User user = createUser(request.getName(), request.getEmail(), request.getPhone(), request.getPassword(),
				Role.SUPERADMIN);
		user.setStatus(UserStatus.ACTIVE);
		String otp = RandomUtils.generateOtp(6, false);
		user.setOtp(otp);
		log.info("Password / OTP({}) ", otp);
		Map<String, Object> model = new HashMap<>();
		model.put("Name", request.getName());
		MailRequest emailrequest = new MailRequest();
		emailrequest.setName(request.getName());
		emailrequest.setSubject("Confirmation Email");
		emailrequest.setTo(request.getEmail());
		emailrequest.setToken(otp);
		// emailService.sendEmail(emailrequest, model);
		user.setOtp(otp);
		repository.save(user);
		return Map.of("statusCode", HttpStatus.CREATED.value() + " ", "isSuccess", Boolean.TRUE, "message",
				"Please Check Registered Email Also Check Spam Folder!!", "otp", otp);
	}


	private void validateDuplicateUser(String email, String phone) {
		Optional<User> checkUser = repository.findByPhoneOrEmail(email, phone);
		if (checkUser.isPresent()) {
			if (checkUser.get().getEmail().equals(email)) {
				throw new RuntimeException("Email already registered: " + email);
			}
//			if (checkUser.get().getPhone().equals(phone)) {
//				throw new RuntimeException("Phone already registered: " + phone);
//			}
		}
	}

	private User createUser(String name, String email, String phone, String password, Role role) {
		User user = new User();
		user.setFullName(name);
		user.setEmail(email);
		user.setPhone(phone);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(role);
		user.setIsAdmin(Boolean.FALSE);
		return user;
	}

	public UserAuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		validateUserStatus(user);
		var jwtToken = jwtService.generateToken(user);
		// revokeAllUserTokens(user);
		// saveUserToken(user, jwtToken);
		return UserAuthenticationResponse.builder().accessToken(jwtToken)
				.data(mapper.map(user, LoggedInUserDetails.class)).build();

	}

	private void saveUserToken(User user, String jwtToken) {
		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.repository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

	public String logout(String email) {
		log.info(">>logout({})", email);
		User user = repository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User Not Found By : " + email));
		user.getTokens().clear();
		return "Logout Successfully";
	}

	private void validateUserStatus(User user) {
		switch (user.getStatus()) {
		case ACTIVE -> {
			// Allowed to proceed
		}
		case PENDING_VERIFICATION -> throw new ServiceException(
				"Your account is pending verification. Please verify your email or phone number to continue.");
		case INACTIVE -> throw new ServiceException(
				"Your account has been deactivated by the administrator. Please contact support to regain access.");
		case SUSPENDED -> throw new ServiceException(
				"Your account has been temporarily suspended due to policy violations. Please contact support for assistance.");
		case BLOCKED -> throw new ServiceException(
				"Your account has been permanently blocked by the administrator. Please contact support for further clarification.");
		case DELETED -> throw new ServiceException(
				"This account is no longer available. Please contact support if you believe this is an error.");
		}
	}
	// Utility method to get logged in user details

}
