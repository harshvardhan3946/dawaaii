package com.dawaaii.service.auth.model;

import java.io.Serializable;


/**
 *
 */
public class AuthenticationResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private AuthenticationStatus status;

	private AccessToken accessToken;

	private String infoMsg;

	public AuthenticationStatus getStatus() {
		return status;
	}

	public void setStatus(AuthenticationStatus authenticationStatus) {
		this.status = authenticationStatus;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

}
