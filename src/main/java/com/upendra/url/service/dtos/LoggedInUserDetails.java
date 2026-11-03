package com.upendra.url.service.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.upendra.url.service.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoggedInUserDetails {

	private String fullName;

	private String email;

	private String phone;

	private String city;

	private Role role;

}
