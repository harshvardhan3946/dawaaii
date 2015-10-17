package com.dawaaii.service.otp;

import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;
import com.dawaaii.service.user.model.User;

/**
 * Created by hojha on 18/05/15.
 */
public interface OTPRequestService {

    boolean isUserEligibleForOtpRequest(User user, OTPType otpType, OTPSource source);

    void resetOtpRequestCount(User user, OTPType otpType, OTPSource source);
}
