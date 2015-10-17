package com.dawaaii.service.user;

import com.dawaaii.service.user.model.UserOTP;

import java.util.Optional;

/**
 * Created by rohit on 21/5/15.
 */
public interface UserOneTimePasswordService {

    Optional<UserOTP> findByOtpUniqueCode(String otpUniqueCode);

}
