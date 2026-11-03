package com.upendra.url.service.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upendra.url.service.services.ShortUrlService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class FreeController {

	@Autowired
	private ShortUrlService shortUrlService;

	@GetMapping("/health")
	public ResponseEntity<Map<String, String>> healthCheck() {
		log.debug("Health check probe invoked");
		Map<String, String> status = new HashMap<>();
		status.put("status", "UP");
		return ResponseEntity.ok(status);
	}

	@GetMapping("/{code}")
	public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable("code") String code) {
		log.info("Redirect request received for code: '{}'", code);
		String originalUrl = shortUrlService.getUrlByCode(code);
		log.info("Redirecting code '{}' -> '{}'", code, originalUrl);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
	}

}