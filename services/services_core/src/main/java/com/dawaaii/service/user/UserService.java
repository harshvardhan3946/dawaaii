package com.dawaaii.service.user;

import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.OTPType;
import com.dawaaii.service.user.model.User;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> findUserById(long userId);

    User getUserById(Long id);

    User saveUser(User user);

    long getUsersCount();

    User getUserByEmail(String email);

    Optional<User> findUserByEmailAndPassword(User user);

    void confirmEmail(String email, String otp);

    void generateOtp(String email, OTPType otpType, OTPSource source);

    void resetPasswordByOldPassword(User user, String oldPassword);

    void validateAndResetPasswordByOTP(String optUniqueCode, OTPSource otpSource, User user);
}