package com.upendra.url.service.dtos;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class PageResponse<T> {

	private List<T> content;
	private long totalElements;
	private int totalPages;
	private int pageNumber;
	private int pageSize;
	private boolean last;

	public static <T> PageResponse<T> from(Page<T> page) {
		PageResponse<T> response = new PageResponse<>();
		response.setContent(page.getContent());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setLast(page.isLast());
		return response;
	}
}
