package com.dawaaii.service.dao.jpa;

import com.dawaaii.service.user.model.OTPRequestCount;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by hojha on 18/05/15.
 */
public interface OTPRequestCountRepository extends JpaRepository<OTPRequestCount, Long> {

    Optional<OTPRequestCount> findByUserAndOtpTypeAndOtpSource(User user, OTPType otpType, OTPSource otpSource);
}
