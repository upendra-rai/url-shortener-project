package com.upendra.url.service.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentClassRequest {
	private Integer studentId;
	private Long classId;
	private Long sectionId;
	private String academicYear;
}