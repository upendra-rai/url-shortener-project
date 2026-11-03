
package com.upendra.url.service.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upendra.url.service.dtos.PageResponse;
import com.upendra.url.service.dtos.ShortUrlRequest;
import com.upendra.url.service.dtos.ShortUrlResponse;
import com.upendra.url.service.services.ShortUrlService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/short-urls")
@RequiredArgsConstructor
@Validated
public class ShortUrlController {

	private final ShortUrlService shortUrlService;

	// Get All Short URLs With Pagination
	@GetMapping
	public ResponseEntity<PageResponse<ShortUrlResponse>> getAllShortUrls(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
		return ResponseEntity.ok(shortUrlService.getAllShortUrls(pageable));
	}

	// Create Short URL
	@PostMapping
	public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody ShortUrlRequest request) {
		ShortUrlResponse response = shortUrlService.createShortUrl(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Get Short URL By ID
	@GetMapping("/{id}")
	public ResponseEntity<ShortUrlResponse> getShortUrlById(@PathVariable Long id) {
		return ResponseEntity.ok(shortUrlService.getShortUrlById(id));
	}

	// Get Short URLs By User ID
	@GetMapping("/user/{userId}")
	public ResponseEntity<PageResponse<ShortUrlResponse>> getShortUrlsByUser(@PathVariable Integer userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		return ResponseEntity.ok(shortUrlService.getShortUrlsByUser(userId, pageable));
	}

	// Update Short URL
	@PutMapping("/{id}")
	public ResponseEntity<ShortUrlResponse> updateShortUrl(@PathVariable Long id,
			@Valid @RequestBody ShortUrlRequest request) {
		return ResponseEntity.ok(shortUrlService.updateShortUrl(id, request));
	}

	// Delete Short URL
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteShortUrl(@PathVariable Long id) {
		shortUrlService.deleteShortUrl(id);
		return ResponseEntity.noContent().build();
	}
}
