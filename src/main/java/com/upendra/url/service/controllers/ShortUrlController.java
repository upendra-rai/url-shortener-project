
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("REST request to get all short URLs - page: {}, size: {}", page, size);
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
		PageResponse<ShortUrlResponse> response = shortUrlService.getAllShortUrls(pageable);
		log.debug("Fetched {} short URLs for page {}", response.getTotalElements(), page);
		return ResponseEntity.ok(response);
	}

	// Create Short URL
	@PostMapping
	public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody ShortUrlRequest request) {
		log.info("REST request to create short URL for target: {}", request.getOriginalUrl());
		ShortUrlResponse response = shortUrlService.createShortUrl(request);
		log.info("Short URL created successfully with ID: {}, code: {}", response.getId(), response.getShortCode());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Get Short URL By ID
	@GetMapping("/{id}")
	public ResponseEntity<ShortUrlResponse> getShortUrlById(@PathVariable Long id) {
		log.info("REST request to get short URL by ID: {}", id);
		ShortUrlResponse response = shortUrlService.getShortUrlById(id);
		return ResponseEntity.ok(response);
	}

	// Get Short URLs By User ID
	@GetMapping("/user/{userId}")
	public ResponseEntity<PageResponse<ShortUrlResponse>> getShortUrlsByUser(@PathVariable Integer userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		log.info("REST request to get short URLs for userId: {} - page: {}, size: {}", userId, page, size);
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		PageResponse<ShortUrlResponse> response = shortUrlService.getShortUrlsByUser(userId, pageable);
		return ResponseEntity.ok(response);
	}

	// Update Short URL
	@PutMapping("/{id}")
	public ResponseEntity<ShortUrlResponse> updateShortUrl(@PathVariable Long id,
			@Valid @RequestBody ShortUrlRequest request) {
		log.info("REST request to update short URL ID: {} with new URL: {}", id, request.getOriginalUrl());
		ShortUrlResponse response = shortUrlService.updateShortUrl(id, request);
		log.info("Short URL ID: {} updated successfully", id);
		return ResponseEntity.ok(response);
	}

	// Delete Short URL
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteShortUrl(@PathVariable Long id) {
		log.info("REST request to delete short URL ID: {}", id);
		shortUrlService.deleteShortUrl(id);
		log.info("Short URL ID: {} deleted successfully", id);
		return ResponseEntity.noContent().build();
	}
}
