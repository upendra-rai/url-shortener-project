package com.upendra.url.service.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {
	private String code;
	private String password;
	private String confirmPassword;

}
