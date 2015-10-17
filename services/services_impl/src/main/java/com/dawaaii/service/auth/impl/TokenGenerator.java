package com.dawaaii.service.auth.impl;

import com.dawaaii.service.auth.model.AccessToken;
import com.hazelcast.util.Base64;
import com.dawaaii.service.auth.datetime.DawaiiDateTime;
import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenGenerator {

    public static final long ACCESS_TOKEN_EXPIRY_DAYS = 30;
    public static final long USER_OTP_EXPIRY_DAYS = 1;
    public static final long USER_OTP_EXPIRY_MINUTES = 15;

    public AccessToken generateAccessToken(User user) {
        AccessToken accessToken = new AccessToken();
        accessToken.setUser(user);
        accessToken.setToken(generateRandomToken());
        accessToken.setExpiresOn(generateExpiresOnTime(ACCESS_TOKEN_EXPIRY_DAYS));
        return accessToken;
    }

    private Date generateExpiresOnTime(long days) {
        Instant instant = DawaiiDateTime.now().plusDays(days).toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    private Date generateExpiresOnTimeWithMinutes(long minutes){
        Instant instant = DawaiiDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    private String generateRandomToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return new String(Base64.encode(bytes));
    }

    public UserOTP generateOTP(User user, OTPType otpType, OTPSource source) {
        UserOTP userOTP = new UserOTP();
        userOTP.setUser(user);
        userOTP.setOTP(RandomStringUtils.randomNumeric(6));
        userOTP.setExpiresOn(generateExpiresOnTime(USER_OTP_EXPIRY_DAYS));
        userOTP.setOtpType(otpType);
        userOTP.setOtpSource(source);
        userOTP.setOtpUniqueCode(UUID.randomUUID().toString());
        return userOTP;
    }

    public UserOTP generateOTPForMobilePayment(User user){
        UserOTP userOTP = new UserOTP();
        userOTP.setUser(user);
        userOTP.setOTP(RandomStringUtils.randomNumeric(6));
        userOTP.setExpiresOn(generateExpiresOnTimeWithMinutes(USER_OTP_EXPIRY_MINUTES));
        userOTP.setOtpType(OTPType.PAYMENT_MOBILE);
        userOTP.setOtpSource(OTPSource.MOBILE);
        userOTP.setOtpUniqueCode(UUID.randomUUID().toString());
        return userOTP;
    }
}
