package com.dawaaii.service.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.dawaaii.service.common.model.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(name = "name", unique=true)
    private String name;

    @Column(name = "code", nullable = false, unique=true)
    private String code;

    @ElementCollection(targetClass = Permission.class)
    @CollectionTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission", nullable = false, unique=false)
    @Enumerated(EnumType.STRING)
    private List<Permission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Permission> getPermissions() {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
