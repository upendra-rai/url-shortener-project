package com.upendra.url.service.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseData<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("statusCode")
	private String statusCode;

	@JsonProperty("message")
	private String message;

	@JsonProperty("result")
	private T result;
	
	
}
