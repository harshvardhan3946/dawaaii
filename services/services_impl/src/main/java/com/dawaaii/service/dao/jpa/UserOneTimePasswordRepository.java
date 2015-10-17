package com.dawaaii.service.dao.jpa;

import java.util.Date;

import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOneTimePasswordRepository extends JpaRepository<UserOTP, Long> {

    UserOTP findByUserIdAndOtpAndOtpTypeAndUsedAndExpiresOnGreaterThan(Long userId, String otp, OTPType otpType, boolean used, Date expiresOn);

    UserOTP findByUserIdAndOtpUniqueCodeAndOtpTypeAndUsedAndExpiresOnGreaterThan(Long userId, String otp, OTPType otpType, boolean used, Date expiresOn);

    UserOTP findByUserIdAndOtpTypeAndUsedAndExpiresOnGreaterThan(Long userId, OTPType forgotPassword, boolean b, Date now);


	UserOTP findByUserAndOtpTypeAndOtpSourceAndOtpUniqueCodeAndUsedAndExpiresOnGreaterThan(
			User user, OTPType otpType, OTPSource otpSource, String optUniqueCode,
			boolean used, Date expiresOn);
	
	UserOTP findByUserAndOtpTypeAndOtpSourceAndOtpAndUsedAndExpiresOnGreaterThan(
			User user, OTPType otpType, OTPSource otpSource, String otp,
			boolean used, Date expiresOn);
	
	UserOTP findByOtpUniqueCodeAndUsedAndExpiresOnGreaterThan(String otpUniqueCode, boolean b, Date now);

    UserOTP findByOtpUniqueCode(String otpUniqueCode);

    UserOTP findByOtpUniqueCodeAndUsedAndOtpTypeAndExpiresOnGreaterThan(String otpUniqueCode, boolean b, OTPType otpType, Date date);

    UserOTP findByUserIdAndOtpTypeAndOtpSourceAndUsedAndExpiresOnGreaterThan(Long userId, OTPType forgotPassword, OTPSource otpSource, boolean b, Date date);
}