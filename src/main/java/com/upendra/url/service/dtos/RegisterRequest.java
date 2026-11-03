package com.upendra.url.service.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
	
	@NotBlank(message = "Name is required.")
	@Size(max = 50, message = "Name must not exceed 50 characters.")
	private String name;

	@NotBlank(message = "Email is required.")
	@Email(message = "Email should be valid.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Email should be valid and contain only allowed characters.")
	@Size(max = 50, message = "Email must not exceed 50 characters.")
	private String email;
	
	@NotBlank(message = "Phone Number is required.")
	@Pattern(regexp = "^\\d{10}$", message = "Phone Number must be exactly 10 digits.")
	private String phone;

	@NotBlank(message = "Password is required.")
	@Size(max = 30, message = "Password not exceed 30 characters.")
	private String password;

}