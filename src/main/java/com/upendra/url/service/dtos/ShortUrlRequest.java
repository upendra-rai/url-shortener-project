
package com.upendra.url.service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlRequest {

	@NotBlank(message = "Original URL is required")
	@Pattern(regexp = "^https?://.+", message = "Please provide a valid HTTP or HTTPS URL")
	private String originalUrl;

	private Integer userId;
}
