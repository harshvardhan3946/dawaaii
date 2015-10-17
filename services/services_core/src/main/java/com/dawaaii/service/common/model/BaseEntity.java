package com.dawaaii.service.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.dawaaii.service.cache.CacheableEntity;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects needing this property.
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class BaseEntity implements TimeStampedAuditable, Serializable, CacheableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
    
    @Column(name = "createdon", nullable = false)
    protected Date createdOn;
    
    @Column(name = "updatedon")
    protected Date updatedOn;

	public void setId(Long id) {
        this.id = id;
    }

	public Long getId() {
        return id;
    }

    public boolean isNew() {
        return (this.id == null);
    }

	@Override
	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public Date getUpdatedOn() {
		return updatedOn;
	}

	@Override
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	@Override
	public String getCacheKey() {
		return getId() == null ? "null" : String.valueOf(getId());
	}
}