package com.dawaaii.service.dao.jpa;

import com.dawaaii.service.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByCode(String code);

    List<Role> findAll();

    Role findByName(String name);
}
