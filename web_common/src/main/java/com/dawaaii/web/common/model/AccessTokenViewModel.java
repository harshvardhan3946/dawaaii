package com.dawaaii.web.common.model;


import com.dawaaii.service.auth.model.AccessToken;

public class AccessTokenViewModel {
    private final String token;
    private final Long userId;

    public AccessTokenViewModel(AccessToken accessToken) {
        token = accessToken.getToken();
        userId = accessToken.getUser().getId();
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }
}
