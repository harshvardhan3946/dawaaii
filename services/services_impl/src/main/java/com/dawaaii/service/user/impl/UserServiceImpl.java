package com.dawaaii.service.user.impl;

import com.dawaaii.service.Messages;
import com.dawaaii.service.auth.impl.TokenGenerator;
import com.dawaaii.service.dao.jpa.UserOneTimePasswordRepository;
import com.dawaaii.service.dao.jpa.UserRepository;
import com.dawaaii.service.exception.CoreException;
import com.dawaaii.service.hash.Encoder;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.otp.OTPRequestService;
import com.dawaaii.service.security.DawaaiiRoleService;
import com.dawaaii.service.security.model.Role;
import com.dawaaii.service.security.model.RoleCode;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserOneTimePasswordRepository userOneTimePasswordRepository;
    private final EmailService asyncMailService;
    private final TokenGenerator tokenGenerator;
    private final DawaaiiRoleService dawaaiiRoleService;
    private final OTPRequestService otpRequestService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserOneTimePasswordRepository userOneTimePasswordRepository,
                           EmailService asyncMailService,
                           TokenGenerator tokenGenerator,
                           DawaaiiRoleService dawaaiiRoleService,
                           OTPRequestService otpRequestService) {
        this.userRepository = userRepository;
        this.userOneTimePasswordRepository = userOneTimePasswordRepository;
        this.asyncMailService = asyncMailService;
        this.tokenGenerator = tokenGenerator;
        this.dawaaiiRoleService = dawaaiiRoleService;
        this.otpRequestService = otpRequestService;
    }

    @Transactional
    @Override
    public User createUser(User user) {
        throwExceptionIfUserEmailAlreadyExists(user);
        user.generatePasswordHash();
        //All users marked as active on creation.
        user.setActive(true);
        if (!user.getCreationSource().equals(UserCreationSource.ADMIN_PORTAL)) {
            Role role = dawaaiiRoleService.getRoleByCode(RoleCode.WEBSITE_APP_GENERAL_USER);
            Assert.notNull(role, "role reference data cannot be null");
            user.addRole(role);
        }
        User savedUser = userRepository.save(user);
       /* if (!savedUser.isEmailConfirmed()) {
            UserOTP userOTP = tokenGenerator.generateOTP(savedUser, OTPType.CONFIRM_EMAIL, OTPSource.getSource(user.getCreationSource()));
            saveOtpAndSendEmail(savedUser, userOTP);
            initOtpRequestCount(user, OTPType.CONFIRM_EMAIL, userOTP.getOtpSource());
        }*/
        //create default preferences for this user
        asyncMailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
        LOG.info("Created new user {}", user.getEmail());
        return savedUser;
    }

    private void throwExceptionIfUserEmailAlreadyExists(User user) {
        String userEmail = user.getEmail();
        if (StringUtils.isEmpty(userEmail)) {
            LOG.error("User email entered is blank while creating user {}", user.getEmail());
            Assert.notNull(userEmail, "User email can not be NULL while creating user!");
            return;
        }

        User userByUserEmail = userRepository.findByEmail(userEmail);
        if (userByUserEmail != null) {
            LOG.error("Trying to create user with duplicate email, user already exists for {}", user.getEmail());
            throw new CoreException(Messages.USER_EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public void confirmEmail(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new CoreException(Messages.USER_NOT_FOUND);

        Date now = new Date();
        UserOTP userOTP = userOneTimePasswordRepository.findByUserIdAndOtpAndOtpTypeAndUsedAndExpiresOnGreaterThan(
                user.getId(), otp, OTPType.CONFIRM_EMAIL, false, now);
        if (userOTP == null) throw new CoreException(Messages.INVALID_OTP);
        markOtpUsedAndSendWelcomeEmail(user, userOTP);
    }

    @Transactional
    @Override
    public void validateAndResetPasswordByOTP(String optUniqueCodeOrOtp, OTPSource otpSource, User user) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (!userOptional.isPresent()) {
            throw new CoreException(Messages.USER_NOT_FOUND);
        }

        User existingUser = userOptional.get();

        Optional<UserOTP> userOTPOptional;
        if (otpSource.equals(OTPSource.MOBILE)) {
            userOTPOptional = Optional.ofNullable(userOneTimePasswordRepository.
                    findByUserAndOtpTypeAndOtpSourceAndOtpAndUsedAndExpiresOnGreaterThan(existingUser, OTPType.FORGOT_PASSWORD, otpSource, optUniqueCodeOrOtp, false, new Date()));
        } else {
            userOTPOptional = Optional.ofNullable(userOneTimePasswordRepository.
                    findByUserAndOtpTypeAndOtpSourceAndOtpUniqueCodeAndUsedAndExpiresOnGreaterThan(existingUser, OTPType.FORGOT_PASSWORD, otpSource, optUniqueCodeOrOtp, false, new Date()));
        }

        if (!userOTPOptional.isPresent()) {
            throw new CoreException(Messages.OTP_NOT_EXIST);
        }

        UserOTP userOTP = userOTPOptional.get();
        if (!Objects.equals(userOTP.getUser().getId(), existingUser.getId())) {
            throw new CoreException(Messages.OPERATION_NOT_ALLOWED);
        }
        existingUser.setPassword(user.getPassword());
        resetPasswordByOtp(existingUser, userOTP);
    }

    @Override
    @Transactional
    public void generateOtp(String email, OTPType otpType, OTPSource source) {
        Optional<User> userByEmail = Optional.ofNullable(userRepository.findByEmail(email));

        if (!userByEmail.isPresent()) {
            throw new CoreException(Messages.EMAIL_NOT_EXISTS);
        }
        Date now = new Date();
        UserOTP userOTP = userOneTimePasswordRepository.findByUserIdAndOtpTypeAndOtpSourceAndUsedAndExpiresOnGreaterThan(userByEmail.get().getId(), otpType, source, false, now);

        if (userOTP == null)
            userOTP = tokenGenerator.generateOTP(userByEmail.get(), otpType, source);

        saveOtpAndSendEmail(userByEmail.get(), userOTP);
    }

    private void saveOtpAndSendEmail(User user, UserOTP userOTP) {
        userOneTimePasswordRepository.save(userOTP);

        asyncMailService.sendUserOTPEmail(user, userOTP);
    }

    private void resetPasswordByOtp(User user, UserOTP userOTP) {
        Date now = new Date();

        generatePasswordHashAgainAndSave(user, user.getPassword());

        userOTP.setUsed(true);
        userOTP.setUsedOn(now);
        userOneTimePasswordRepository.save(userOTP);
        initOtpRequestCount(user, OTPType.FORGOT_PASSWORD, userOTP.getOtpSource());
    }

    private void generatePasswordHashAgainAndSave(User existingUser, String password) {
        existingUser.setPassword(password);
        existingUser.generatePasswordHash();
        userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void resetPasswordByOldPassword(User user, String oldPassword) {
        User existingUser = userRepository.findOne(user.getId());
        if (existingUser == null) throw new CoreException(Messages.USER_NOT_FOUND);

        checkIfUserKnowsHisOldPassword(existingUser, oldPassword);
        generatePasswordHashAgainAndSave(existingUser, user.getPassword());
    }

    private void checkIfUserKnowsHisOldPassword(User existingUser, String oldPassword) {
        String hash = Encoder.hash(oldPassword, existingUser.getPasswordSalt());
        if (!hash.equals(existingUser.getPasswordHash()))
            throw new CoreException(Messages.INVALID_USER_PASSWORD);
    }

    @Override
    public Optional<User> findUserById(long userId) {
        User user = userRepository.findOne(userId);
        return Optional.ofNullable(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public long getUsersCount() {
        return userRepository.count();
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(User user) {
        return getUserByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    private Optional<User> getUserByEmailAndPassword(String email, String password) {
        Optional<User> userByEmail = Optional.ofNullable(userRepository.findByEmail(email));
        if (userByEmail.isPresent()) {
            String hash = Encoder.hash(password, userByEmail.get().getPasswordSalt());
            if (userByEmail.get().getPasswordHash().equals(hash)) return userByEmail;
        }
        return Optional.empty();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void initOtpRequestCount(User user, OTPType otpType, OTPSource otpSource) {
        otpRequestService.resetOtpRequestCount(user, otpType, otpSource);
    }

    private void markOtpUsedAndSendWelcomeEmail(User user, UserOTP userOTP) {
        user.setEmailConfirmed(true);
        userRepository.save(user);
        userOTP.setUsed(true);
        userOTP.setUsedOn(new Date());
        userOneTimePasswordRepository.save(userOTP);
        initOtpRequestCount(user, OTPType.CONFIRM_EMAIL, userOTP.getOtpSource());
//        asyncMailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
    }
}
