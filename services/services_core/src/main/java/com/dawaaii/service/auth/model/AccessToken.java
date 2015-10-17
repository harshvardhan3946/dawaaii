package com.dawaaii.service.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dawaaii.service.common.model.BaseEntity;
import com.dawaaii.service.user.model.User;

@Entity
@Table(name = "accesstoken")
public class AccessToken extends BaseEntity {

	@Column(name = "token", nullable = false, length = 64)
	private String token;

	@Column(name = "expireson")
	private Date expiresOn;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", nullable = false)
	private User user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
