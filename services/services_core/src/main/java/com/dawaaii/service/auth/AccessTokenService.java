package com.dawaaii.service.auth;

import com.dawaaii.service.auth.model.AccessToken;
import com.dawaaii.service.user.model.User;

public interface AccessTokenService {
    AccessToken getAccessTokenForUser(User user);
}
