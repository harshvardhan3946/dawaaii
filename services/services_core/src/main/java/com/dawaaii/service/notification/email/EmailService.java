package com.dawaaii.service.notification.email;

import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;


public interface EmailService {

    void sendUserOTPEmail(User user, UserOTP userOTP);
}