package com.upendra.url.service.enums;

public enum UserStatus {

	ACTIVE, // User is fully active and can use system

	INACTIVE, // Temporarily inactive (manual or system action)

	PENDING_VERIFICATION, // Registered but email/phone not verified

	SUSPENDED, // Suspended due to violation

	BLOCKED, // Permanently blocked

	DELETED // Soft deleted (never hard delete in production)
}
