package com.upendra.url.service.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime created;

	@Column(name = "updated", nullable = false)
	private LocalDateTime updated;

	@Column(name = "is_active")
	private boolean isActive = true;

	@PrePersist
	protected void onCreate() {
		this.created = LocalDateTime.now();
		this.updated = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updated = LocalDateTime.now();
	}

}
