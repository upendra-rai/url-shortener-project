package com.upendra.url.service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

	SUPERADMIN_READ("superadmin:read"), SUPERADMIN_UPDATE("super:update"), SUPERADMIN_CREATE("super:create"),
	SUPERADMIN_DELETE("super:delete"),

	ADMIN_READ("admin:read"), ADMIN_UPDATE("admin:update"), ADMIN_CREATE("admin:create"), ADMIN_DELETE("admin:delete"),

	TEACHER_READ("teacher:read"), TEACHER_UPDATE("teacher:update"), TEACHER_CREATE("teacher:create"),
	TEACHER_DELETE("teacher:delete"),

	STUDENT_READ("student:read"), STUDENT_UPDATE("student:update"), STUDENT_CREATE("student:create"),
	STUDENT_DELETE("student:delete"),

	PRINCIPAL_READ("principal:read"), PRINCIPAL_UPDATE("principal:update"), PRINCIPAL_CREATE("principal:create"),
	PRINCIPAL_DELETE("principal:delete"),

	PARENT_READ("parent:read"), PARENT_UPDATE("parent:update"), PARENT_CREATE("parent:create"),
	PARENT_DELETE("parent:delete"),

	STAFF_READ("staff:read"), STAFF_UPDATE("staff:update"), STAFF_CREATE("staff:create"), STAFF_DELETE("staff:delete");

	@Getter
	private final String permission;
}
