package com.upendra.url.service.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_Log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entity_name", nullable = false)
	private String entityName;
	
	@Column(name = "entity_id", nullable = false)
	private String entityId;
	
	@Column(name = "field_name", nullable = false)
	private String fieldName;
	
	private String oldValue;
	
	private String newValue;

	@Column(name = "changed_by", nullable = false)
	private String changedBy;

	@JsonFormat(pattern = "d MMMM yyyy HH:mm", timezone = "Asia/Kolkata")
	private LocalDateTime changedAt;

	@PrePersist
	public void prePersist() {
		this.changedAt = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
	}
}