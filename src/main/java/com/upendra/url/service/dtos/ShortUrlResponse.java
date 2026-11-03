
package com.upendra.url.service.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlResponse {

	private Long id;

	private String shortCode;

	private String shortUrl;

	private String originalUrl;

	private Long clickCount;

	private LocalDateTime expiresAt;

	private LocalDateTime createdAt;

	private Boolean isActive;

	private Integer userId;
}
