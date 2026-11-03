package com.upendra.url.service.dtos;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteRequest {

	private LocalDate homeworkDate;

	private List<String> questions;

}