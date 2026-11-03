package com.upendra.url.service.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upendra.url.service.dtos.PageResponse;
import com.upendra.url.service.dtos.ShortUrlRequest;
import com.upendra.url.service.dtos.ShortUrlResponse;
import com.upendra.url.service.entities.ShortUrl;
import com.upendra.url.service.entities.User;
import com.upendra.url.service.exceptions.ResourceNotFoundException;
import com.upendra.url.service.repositories.UrlRepository;
import com.upendra.url.service.repositories.UserRepository;
import com.upendra.url.service.security.DistributedIdGenerator;
import com.upendra.url.service.util.Base62Encoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShortUrlService {

	@Autowired
	private UrlRepository shortUrlRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DistributedIdGenerator idGenerator;
	@Autowired
	private Base62Encoder base62Encoder;

	@Autowired(required = false)
	private CacheManager cacheManager;

	@Value("${app.short-url.base-url:http://localhost:8080}")
	private String baseUrl;

	@Transactional
	public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
		log.info("Creating short URL for: {}", request.getOriginalUrl());
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.setOriginalUrl(request.getOriginalUrl());

		try {
			long id = idGenerator.nextId();
			String shortCode = base62Encoder.encode(id);
			log.debug("Allocated distributed ID: {} -> base62 code: {}", id, shortCode);
			shortUrl.setShortCode(shortCode);
		} catch (Exception e) {
			log.error("Failed to generate short URL ID for originalUrl: {}", request.getOriginalUrl(), e);
			throw new IllegalStateException("Unable to generate short URL ID", e);
		}

		shortUrl.setClickCount(0L);
		shortUrl.setExpiresAt(LocalDateTime.now().plusYears(10));

		ShortUrl saved = shortUrlRepository.save(shortUrl);
		log.info("Persisted short URL: id={}, code={}, target={}", saved.getId(), saved.getShortCode(), saved.getOriginalUrl());

		return mapToResponse(saved);
	}

	@Transactional(readOnly = true)
	public ShortUrlResponse getShortUrlById(Long id) {
		log.debug("Fetching short URL entity by ID: {}", id);
		ShortUrl shortUrl = getShortUrl(id);
		return mapToResponse(shortUrl);
	}

	@Transactional(readOnly = true)
	public PageResponse<ShortUrlResponse> getAllShortUrls(Pageable pageable) {
		log.debug("Fetching short URLs page: {}", pageable);
		Page<ShortUrlResponse> page = shortUrlRepository.findAll(pageable).map(this::mapToResponse);
		return PageResponse.from(page);
	}

	@Transactional(readOnly = true)
	public PageResponse<ShortUrlResponse> getShortUrlsByUser(Integer userId, Pageable pageable) {
		log.debug("Fetching short URLs for userId: {}, pageable: {}", userId, pageable);
		if (!userRepository.existsById(userId)) {
			log.warn("User not found with id: {}", userId);
			throw new ResourceNotFoundException("User not found");
		}
		Page<ShortUrlResponse> page = shortUrlRepository.findByUser_Id(userId, pageable).map(this::mapToResponse);
		return PageResponse.from(page);
	}

	public ShortUrlResponse updateShortUrl(Long id, ShortUrlRequest request) {
		log.info("Updating short URL ID: {} with new target URL: {}", id, request.getOriginalUrl());
		ShortUrl shortUrl = getShortUrl(id);
		shortUrl.setOriginalUrl(request.getOriginalUrl());
		ShortUrl updatedUrl = shortUrlRepository.save(shortUrl);
		evictUrlFromCache(shortUrl.getShortCode());
		log.info("Updated short URL ID: {} successfully", id);
		return mapToResponse(updatedUrl);
	}

	public void deleteShortUrl(Long id) {
		log.info("Deleting short URL ID: {}", id);
		ShortUrl shortUrl = getShortUrl(id);
		evictUrlFromCache(shortUrl.getShortCode());
		shortUrlRepository.delete(shortUrl);
		log.info("Deleted short URL ID: {} and code: {} successfully", id, shortUrl.getShortCode());
	}

	private void evictUrlFromCache(String code) {
		if (code != null && cacheManager != null) {
			Cache cache = cacheManager.getCache("urls");
			if (cache != null) {
				cache.evict(code);
				log.debug("Evicted short code '{}' from Redis cache 'urls'", code);
			}
		}
	}

	private ShortUrl getShortUrl(Long id) {
		return shortUrlRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("Short URL not found with id: {}", id);
					return new ResourceNotFoundException("Short URL not found with id: " + id);
				});
	}

	private ShortUrlResponse mapToResponse(ShortUrl shortUrl) {
		return ShortUrlResponse.builder().id(shortUrl.getId()).shortCode(shortUrl.getShortCode())
				.shortUrl(baseUrl + "/" + shortUrl.getShortCode()).originalUrl(shortUrl.getOriginalUrl())
				.clickCount(shortUrl.getClickCount()).expiresAt(shortUrl.getExpiresAt())
				.isActive(shortUrl.isActive())
				.createdAt(shortUrl.getCreated()).userId(shortUrl.getUser() != null ? shortUrl.getUser().getId() : null)
				.build();
	}

	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static final int SHORT_CODE_LENGTH = 8;

	public ShortUrlResponse createShortUrlOLD(ShortUrlRequest request) {
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.setOriginalUrl(request.getOriginalUrl());
		shortUrl.setShortCode(generateShortCode());
		shortUrl.setExpiresAt(LocalDateTime.now().plusYears(10));

		if (request.getUserId() != null) {
			User user = userRepository.findById(request.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("User not found"));
			shortUrl.setUser(user);
		}
		shortUrl.setClickCount(0L);
		ShortUrl savedUrl = shortUrlRepository.save(shortUrl);
		return mapToResponse(savedUrl);
	}

	private String generateShortCode() {
		String shortCode;
		SecureRandom random = new SecureRandom();
		do {
			StringBuilder code = new StringBuilder(SHORT_CODE_LENGTH);
			for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
				int index = random.nextInt(CHARACTERS.length());
				code.append(CHARACTERS.charAt(index));
			}
			shortCode = code.toString();
		} while (shortUrlRepository.existsByShortCode(shortCode));
		return shortCode;
	}

	@Cacheable(value = "urls", key = "#code")
	public String getUrlByCode(String code) {
		log.info("Resolving short code from database (cache miss): {}", code);
		ShortUrl sort = shortUrlRepository.findByShortCode(code)
				.orElseThrow(() -> {
					log.warn("Short URL not found for code: {}", code);
					return new ResourceNotFoundException("Short URL not found with code: " + code);
				});
		log.debug("Found original URL for code {}: {}", code, sort.getOriginalUrl());
		return sort.getOriginalUrl();
	}

}
