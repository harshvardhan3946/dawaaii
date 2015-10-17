package com.dawaaii.service.auth.dao;

import com.dawaaii.service.auth.model.AccessToken;
import com.dawaaii.service.dao.jpa.AccessTokenRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Ignore
public class AccessTokenRepositoryTest {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Test
    public void testFindByUserIdAndExpireOnGreaterThan() {
        AccessToken accessToken = accessTokenRepository.findByUserIdAndExpiresOnGreaterThan(1L,
                Date.from(LocalDateTime.of(2015, 1, 1, 0, 0).toInstant(ZoneOffset.UTC)));
        assertThat(accessToken, is(notNullValue()));
        assertNotNull(accessToken.getUser().getId());

    }
}