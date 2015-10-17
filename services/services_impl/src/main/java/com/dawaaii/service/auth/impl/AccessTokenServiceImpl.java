package com.dawaaii.service.auth.impl;

import com.dawaaii.service.auth.AccessTokenService;
import com.dawaaii.service.auth.model.AccessToken;
import com.dawaaii.service.dao.jpa.AccessTokenRepository;
import com.dawaaii.service.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Optional;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    private AccessTokenRepository accessTokenRepository;
    private TokenGenerator tokenGenerator;

    @Autowired
    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository, TokenGenerator tokenGenerator) {
        this.accessTokenRepository = accessTokenRepository;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    @Transactional
    public AccessToken getAccessTokenForUser(User user) {
        Date now = new Date();
        Optional<AccessToken> accessToken = Optional
                .ofNullable(accessTokenRepository
                        .findByUserIdAndExpiresOnGreaterThan(user.getId(), now));
        if (accessToken.isPresent())
            return accessToken.get();

        AccessToken newAccessToken = tokenGenerator.generateAccessToken(user);
        accessTokenRepository.save(newAccessToken);
        return newAccessToken;
    }
}
