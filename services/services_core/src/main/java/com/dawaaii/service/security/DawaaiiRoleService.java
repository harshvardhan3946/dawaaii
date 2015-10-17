package com.dawaaii.service.security;

import java.util.List;

import com.dawaaii.service.security.model.Permission;
import com.dawaaii.service.security.model.Role;
import com.dawaaii.service.security.model.RoleCode;

public interface DawaaiiRoleService {

	Role createRole(String roleName, String code, List<Permission> permissions);

    Role getRoleByCode(RoleCode roleCode);

	List<Role> getAllRole();

	Role getRoleByCode(String roleCode);

	Role saveRole(Role role);
	boolean deleteRole(Role role);

	Role getRoleByName(String string);
}
