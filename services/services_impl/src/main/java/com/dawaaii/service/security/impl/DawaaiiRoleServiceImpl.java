package com.dawaaii.service.security.impl;

import java.util.List;
import java.util.UUID;

import com.dawaaii.service.dao.jpa.RoleRepository;
import com.dawaaii.service.security.model.RoleCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawaaii.service.security.DawaaiiRoleService;
import com.dawaaii.service.security.model.Permission;
import com.dawaaii.service.security.model.Role;
@Service
public class DawaaiiRoleServiceImpl implements DawaaiiRoleService {

	@Autowired
	RoleRepository roleRepository;

	@Transactional
	@Override
	public Role createRole(String roleName, String code,
			List<Permission> permissions) {
		Role role = new Role();
		role.setName(roleName);
		if(code == null) code = UUID.randomUUID().toString();
		role.setCode(code);
		if(permissions != null) role.setPermissions(permissions);
		return roleRepository.save(role);
	}

	@Override
	public Role getRoleByCode(RoleCode role) {
		return roleRepository.findByCode(role.name());
	}
	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}
	@Override
	public Role getRoleByCode(String roleCode) {
		return roleRepository.findByCode(roleCode);
	}
	@Override
	public List<Role> getAllRole() {
		return roleRepository.findAll();
	}
	@Override
	public boolean deleteRole(Role role)
	{
		try{
			roleRepository.delete(role);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}

	@Override
	public Role getRoleByName(String string) {
		// TODO Auto-generated method stub
		return roleRepository.findByName(string);
	}
}
