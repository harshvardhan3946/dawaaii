package com.dawaaii.service.property.model;

import com.dawaaii.service.common.model.BaseEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by rohit on 2/11/15.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "property")
@Cacheable(true)
public class Property extends BaseEntity {

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "value",nullable = false)
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
