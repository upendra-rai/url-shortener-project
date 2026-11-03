package com.upendra.url.service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthCodeRequest {

	@NotBlank(message = "NotBlank.authRequest.email")
	private String phone;
	
	@NotBlank(message = "NotBlank.authRequest.opt")
	private String opt;
}
