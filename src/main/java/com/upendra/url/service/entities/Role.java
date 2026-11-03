package com.upendra.url.service.entities;

import static com.upendra.url.service.enums.Permission.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.upendra.url.service.enums.Permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

	SUPERADMIN(Set.of(ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE, SUPERADMIN_CREATE, SUPERADMIN_DELETE,
			SUPERADMIN_READ, SUPERADMIN_UPDATE)),

	ADMIN(Set.of(ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE)),

	STUDENT(Set.of(STUDENT_READ, STUDENT_UPDATE, STUDENT_DELETE, STUDENT_CREATE)),

	TEACHER(Set.of(TEACHER_READ, TEACHER_UPDATE, TEACHER_DELETE, TEACHER_CREATE)),

	PRINCIPAL(Set.of(PRINCIPAL_READ, PRINCIPAL_UPDATE, PRINCIPAL_DELETE, PRINCIPAL_CREATE)),
	
	PARENT(Set.of(PARENT_READ, PARENT_UPDATE, PARENT_DELETE, PARENT_CREATE)),
	
	STAFF(Set.of(STAFF_READ, STAFF_UPDATE, STAFF_DELETE, STAFF_CREATE));


	@Getter
	private final Set<Permission> permissions;

	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = getPermissions().stream()
				.map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return authorities;
	}
}
