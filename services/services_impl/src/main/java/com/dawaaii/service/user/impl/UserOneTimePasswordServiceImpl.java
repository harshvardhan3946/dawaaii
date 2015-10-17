package com.dawaaii.service.user.impl;

import com.dawaaii.service.user.UserOneTimePasswordService;
import com.dawaaii.service.user.model.UserOTP;
import com.dawaaii.service.dao.jpa.UserOneTimePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Created by rohit on 21/5/15.
 */
@Service(value = "userOneTimePasswordService")
public class UserOneTimePasswordServiceImpl implements UserOneTimePasswordService {
    @Autowired
    private UserOneTimePasswordRepository userOneTimePasswordRepository;
    @Override
    public Optional<UserOTP> findByOtpUniqueCode(String otpUniqueCode) {
        return Optional.ofNullable(userOneTimePasswordRepository.findByOtpUniqueCodeAndUsedAndExpiresOnGreaterThan(otpUniqueCode, false, new Date()));
    }
}
