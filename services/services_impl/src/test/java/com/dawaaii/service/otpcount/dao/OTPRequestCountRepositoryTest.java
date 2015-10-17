package com.dawaaii.service.otpcount.dao;

import com.dawaaii.service.dao.jpa.OTPRequestCountRepository;
import com.dawaaii.service.dao.jpa.UserRepository;
import com.dawaaii.service.user.model.OTPRequestCount;
import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;
import com.dawaaii.service.user.model.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Ignore
public class OTPRequestCountRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRequestCountRepository otpRequestCountRepository;

    @Test
    public void countOTPForUser() {
        User user = userRepository.findOne(1L);
        Assert.assertNotNull(user);
        OTPRequestCount otpRequestCount = new OTPRequestCount();
        otpRequestCount.setUser(user);
        otpRequestCount.setOtpType(OTPType.CONFIRM_EMAIL);
        otpRequestCount.setOtpSource(OTPSource.WEBSITE);
        otpRequestCount.setRequestCount(otpRequestCount.getRequestCount() + 1);

        OTPRequestCount savedOtpRequestCount = otpRequestCountRepository.save(otpRequestCount);
        Assert.assertNotNull(savedOtpRequestCount.getId());

        Optional<OTPRequestCount> retOtpRequestCount = otpRequestCountRepository.findByUserAndOtpTypeAndOtpSource(user, OTPType.CONFIRM_EMAIL, OTPSource.WEBSITE);
        Assert.assertTrue(retOtpRequestCount.isPresent());
        Assert.assertNotNull(retOtpRequestCount);
        Assert.assertEquals("counting otp request is not proper", 1, retOtpRequestCount.get().getRequestCount());
    }
}
