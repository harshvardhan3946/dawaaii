package com.dawaaii.service.auth.impl;

import static com.dawaaii.service.user.model.OTPType.CONFIRM_EMAIL;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import com.dawaaii.service.auth.datetime.DawaiiDateTime;
import com.dawaaii.service.auth.model.AccessToken;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;
import org.junit.Test;

import com.dawaaii.service.user.model.OTPSource;

public class TokenGeneratorTest {

    @Test
    public void testAccessTokenGenerate() throws Exception {
        TokenGenerator generator = new TokenGenerator();
        User user = new User();
        user.setId(1L);
        AccessToken accessToken = generator.generateAccessToken(user);
        assertThat(accessToken.getToken(), is(notNullValue()));
        assertThat(accessToken.getExpiresOn(), is(notNullValue()));
        assertThat(accessToken.getUser().getId(), is(1L));
    }


    @Test
    public void testGenerateOTP() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DawaiiDateTime.freezeAt(now, () -> {
            TokenGenerator generator = new TokenGenerator();
            User user = new User();
            user.setId(1L);
            UserOTP userOTP = generator.generateOTP(user, CONFIRM_EMAIL, OTPSource.MOBILE);
            assertThat(userOTP.getOTP(), is(notNullValue()));
            LocalDateTime value = now.plusDays(TokenGenerator.USER_OTP_EXPIRY_DAYS);
            Date expectedExpiresOn = Date.from(value.toInstant(ZoneOffset.UTC));
            assertThat(userOTP.getExpiresOn(), is(expectedExpiresOn));
            assertThat(userOTP.getUser().getId(), is(1L));
            assertThat(userOTP.getOtpType(), is(CONFIRM_EMAIL));
        });

    }
}