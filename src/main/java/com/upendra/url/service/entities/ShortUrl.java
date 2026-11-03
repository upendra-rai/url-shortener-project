package com.upendra.url.service.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "short_urls", indexes = { @Index(name = "idx_short_urls_created_at", columnList = "created_at"),
		@Index(name = "idx_short_urls_user_id", columnList = "user_id") })
public class ShortUrl extends BaseEntity {

	@Column(name = "short_code", nullable = false, unique = true, length = 20)
	private String shortCode;

	@Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
	private String originalUrl;

	@Column(name = "click_count", nullable = false)
	@Builder.Default
	private Long clickCount = 0L;

	@Column(name = "expires_at")
	private LocalDateTime expiresAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
