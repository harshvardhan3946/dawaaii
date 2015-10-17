package com.dawaaii.service.otp;

import com.dawaaii.service.dao.jpa.OTPRequestCountRepository;
import com.dawaaii.service.user.model.OTPRequestCount;
import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;
import com.dawaaii.service.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

/**
 * Created by hojha on 18/05/15.
 */
@Service(value = "otpRequestService")
public class OTPRequestServiceImpl implements OTPRequestService {

    @Autowired
    private OTPRequestCountRepository otpRequestCountRepository;
    public static final int REQUEST_THRESHOLD = 3;

    @Override
    public boolean isUserEligibleForOtpRequest(User user, OTPType otpType, OTPSource source) {
        Optional<OTPRequestCount> otpRequestCount = otpRequestCountRepository.findByUserAndOtpTypeAndOtpSource(user, otpType, source);

        if (!otpRequestCount.isPresent()) {
            OTPRequestCount requestCount = new OTPRequestCount();
            requestCount.setUser(user);
            requestCount.setOtpType(otpType);
            requestCount.setRequestCount(1);
            requestCount.setOtpSource(source);
            otpRequestCountRepository.save(requestCount);
            return true;
        }

        OTPRequestCount reqCount = otpRequestCount.get();
        if (reqCount.getUpdatedOn() != null && reqCount.getUpdatedOn().toInstant().isBefore(new Date().toInstant().minus(1L, ChronoUnit.HOURS))) {
            reqCount.setRequestCount(1);
            otpRequestCountRepository.save(reqCount);
            return true;
        } else {
            if (reqCount.getRequestCount() <= REQUEST_THRESHOLD) {
                reqCount.setRequestCount(reqCount.getRequestCount() + 1);
                otpRequestCountRepository.save(reqCount);
                return true;
            } else {
                reqCount.setRequestCount(reqCount.getRequestCount() + 1);
                otpRequestCountRepository.save(reqCount);
                return false;
            }
        }
    }

    public void resetOtpRequestCount(User user, OTPType otpType, OTPSource otpSource) {
        Optional<OTPRequestCount> otpRequestCount = otpRequestCountRepository.findByUserAndOtpTypeAndOtpSource(user, otpType, otpSource);
        OTPRequestCount requestCount;
        if (!otpRequestCount.isPresent()) {
            requestCount = new OTPRequestCount();
        } else {
            requestCount = otpRequestCount.get();
        }
        requestCount.setUser(user);
        requestCount.setOtpType(otpType);
        requestCount.setOtpSource(otpSource);
        requestCount.setRequestCount(0);
        otpRequestCountRepository.save(requestCount);
    }

}
