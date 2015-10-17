package com.dawaaii.service.auth.impl;

import com.dawaaii.service.auth.model.AccessToken;
import com.dawaaii.service.dao.jpa.AccessTokenRepository;
import com.dawaaii.service.user.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AccessTokenServiceImpl.class})
public class AccessTokenServiceImplTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;
    @Mock
    private TokenGenerator tokenGenerator;
    private AccessTokenServiceImpl accessTokenService;

    @Before
    public void setUp() throws Exception {
        accessTokenService = new AccessTokenServiceImpl(accessTokenRepository, tokenGenerator);
    }

    @Test
    public void testGetExistingTokeIfNotExpired() throws Exception {
        User user = new User();
        user.setId(1L);
        AccessToken existingToken = new AccessToken();
        Date now = new Date();
        whenNew(Date.class).withNoArguments().thenReturn(now);
        when(accessTokenRepository.findByUserIdAndExpiresOnGreaterThan(user.getId(), now)).thenReturn(existingToken);
        AccessToken accessToken = accessTokenService.getAccessTokenForUser(user);
        assertThat(accessToken,is(existingToken));
    }

    @Test
    public void testGenerateNewTokenWhenTokenIsExpiredOrHasNotBeenGeneratedYet() throws Exception {
        mockStatic(AccessToken.class);
        User user = new User();
        user.setId(1L);
        when(accessTokenRepository.findByUserIdAndExpiresOnGreaterThan(user.getId(), new Date())).thenReturn(null);
        AccessToken newAccessToken = new AccessToken();
        when(tokenGenerator.generateAccessToken(user)).thenReturn(newAccessToken);
        AccessToken accessToken = accessTokenService.getAccessTokenForUser(user);
        assertThat(accessToken,is(newAccessToken));
        verify(accessTokenRepository).save(newAccessToken);
    }
}