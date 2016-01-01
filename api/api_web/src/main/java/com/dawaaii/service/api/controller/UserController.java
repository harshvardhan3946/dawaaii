package com.dawaaii.service.api.controller;

import com.dawaaii.service.exception.CoreException;
import com.dawaaii.service.otp.OTPRequestService;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.OTPSource;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserCreationSource;
import com.dawaaii.web.common.model.*;
import com.dawaaii.web.common.response.DawaaiiApiResponse;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

import static com.dawaaii.service.Messages.*;
import static com.dawaaii.web.common.response.DawaaiiApiResponse.error;
import static com.dawaaii.web.common.response.DawaaiiApiResponse.success;
import static com.dawaaii.web.common.response.DawaaiiFieldError.parseErrors;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
@Api(value = "/users", description = "user operations")
@RequestMapping(value = "/users")
@Controller
public class UserController extends APIBaseController {

    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

    private static final String USER_ENTITY_KEY = "user";

    public static final String OTP_UNIQUE_CODE = "otp";

    private final UserService userService;

    @Autowired
    private final JdbcTokenStore tokenStore;

    @Autowired
    private OTPRequestService otpRequestService;

    @Autowired
    public UserController(UserService userService, JdbcTokenStore tokenStore) {
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    @ApiOperation(value = "Create user", notes = "permission: ...")
    @RequestMapping(method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> signUp(@Valid @RequestBody UserViewModel userViewModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return error(INVALID_REQUEST).withEntity(ERRORS, parseErrors(bindingResult)).respond();
        }
        try {
            User userEntity = userViewModel.toUserEntity();
            userEntity.setCreationSource(UserCreationSource.WEBSITE);
            User user = userService.createUser(userEntity);

            return success(USER_CREATED_SUCCESSFULLY).withEntity(USER_ENTITY_KEY, new UserViewModel(user)).respond();
        } catch (CoreException ex) {
            if (USER_EMAIL_ALREADY_EXISTS.equals(ex.getMessage())) {
                LOG.error("Error creating user, user already exists!", ex);
                return error(USER_EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT).respond();
            }
            LOG.error("Error creating user!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        } catch (Exception ex) {
            LOG.error("Error creating user!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    @ApiOperation(value = "Update user by id", notes = "permission: ...")
    @RequestMapping(value = "/{id}", method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> updateUser(Authentication authentication, @PathVariable("id") Long userId, @RequestBody UserViewModel userViewModel) {
        try {
            User user = userService.getUserById(authenticatedUserId(authentication));
            user = userService.saveUser(userViewModel.udpateProfile(user));
            return success(USER_UPDATED_SUCCESSFULLY).withEntity(USER_ENTITY_KEY, new UserViewModel(user)).respond();
        } catch (EntityNotFoundException ex) {
            LOG.error("Entity not found!", ex);
            return error(USER_NOT_FOUND, HttpStatus.NOT_FOUND).respond();
        } catch (Exception ex) {
            LOG.error("Error updating user!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    @ApiOperation(value = "Get current logged in user", notes = "permission: ...")
    @RequestMapping(value = "/currentuser", method = GET, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> get(Authentication authentication) {
        Optional<User> user = userService.findUserById(authenticatedUserId(authentication));
        if (user.isPresent()) {
            return success().withEntity(USER_ENTITY_KEY, new UserViewModel(user.get())).respond();
        } else return error(USER_NOT_FOUND, HttpStatus.NOT_FOUND).respond();
    }

    @ApiOperation(value = "confirm email for not logged in user.", notes = "permission: ...")
    @RequestMapping(value = "/confirmemail", method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> confirmEmail(@Valid @RequestBody ConfirmEmailViewModel confirmEmailViewModel,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return error(INVALID_REQUEST).withEntity(ERRORS, parseErrors(bindingResult)).respond();
        }
        try {
            userService.confirmEmail(confirmEmailViewModel.getEmail(), confirmEmailViewModel.getOtp());
            return success(EMAIL_CONFIRMED).respond();
        } catch (CoreException ex) {
            LOG.error("Error confirming email for user!", ex);
            return error(ex.getMessage(), HttpStatus.BAD_REQUEST).respond();
        } catch (Exception ex) {
            LOG.error("Error confirming user email!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    @ApiOperation(value = "forgot password otp email", notes = "permission: ...")
    @RequestMapping(value = "/generateotp", method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> generateOtp(@Valid @RequestBody GenerateOrResendOtpViewModel generateOtpViewModel,
                                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return error(INVALID_REQUEST).withEntity(ERRORS, parseErrors(bindingResult)).respond();
        }
        try {
            userService.generateOtp(generateOtpViewModel.getEmail(), generateOtpViewModel.getOtpType(), OTPSource.MOBILE);
            return success(OTP_SENT).respond();
        } catch (CoreException ex) {
            LOG.debug("email does not exists ! ", ex.getMessage());
            return error(ex.getMessage(), HttpStatus.BAD_REQUEST).respond();
        } catch (Exception ex) {
            LOG.error("Error generating otp!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    @ApiOperation(value = "reset password for logged in user", notes = "permission: ...")
    @RequestMapping(value = "/resetpassword", method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> resetPassword(@Valid @RequestBody ResetPasswordViewModel resetPasswordViewModel,
                                                            BindingResult bindingResult,
                                                            Authentication authentication
    ) {
        if (bindingResult.hasErrors()) {
            return error(INVALID_REQUEST).withEntity(ERRORS, parseErrors(bindingResult)).respond();
        }

        try {
            User user = resetPasswordViewModel.toUserEntity();
            Long userId = authenticatedUserId(authentication);
            if (userId == null) {
                return error(INVALID_REQUEST).respond();
            }

            if (userId != null) {
                user.setId(userId);
                userService.resetPasswordByOldPassword(user, resetPasswordViewModel.getOldPassword());
            }
            return success(PASSWORD_RESET_SUCCESSFULLY).respond();
        } catch (CoreException ex) {
            LOG.error("Error reseting password for user!", ex);
            return error(ex.getMessage(), HttpStatus.BAD_REQUEST).respond();
        } catch (Exception ex) {
            LOG.error("Error reseting password for user!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    @ApiOperation(value = "Forgot password for not logged in user", notes = "permission: ...")
    @RequestMapping(value = "/forgotpassword", method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordViewModel forgotPasswordViewModel,
                                                             BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return error(INVALID_REQUEST).withEntity(ERRORS, parseErrors(bindingResult)).respond();
        }

        try {
            User user = forgotPasswordViewModel.toUserEntity();
            if (forgotPasswordViewModel.getOtp() == null) {
                return error(INVALID_REQUEST).respond();
            }

            String otp = forgotPasswordViewModel.getOtp();
            userService.validateAndResetPasswordByOTP(otp, OTPSource.MOBILE, user);
            revokeAccessToken(forgotPasswordViewModel.getEmail());

            return success(PASSWORD_RESET_SUCCESSFULLY).respond();
        } catch (CoreException ex) {
            LOG.error("Error sneding forgot password for user!", ex);
            return error(ex.getMessage(), HttpStatus.BAD_REQUEST).respond();
        } catch (Exception ex) {
            LOG.error("Error reseting forgot password for user!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    @ApiOperation(value = "resend otp", notes = "permission: ...")
    @RequestMapping(value = "/resendotp", method = POST, headers = ACCEPT_JSON)
    @ResponseBody
    public ResponseEntity<DawaaiiApiResponse> resendOtp(@Valid @RequestBody GenerateOrResendOtpViewModel resendOtpViewModel,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return error(INVALID_REQUEST).withEntity(ERRORS, parseErrors(bindingResult)).respond();
        }

        try {
            User user = userService.getUserByEmail(resendOtpViewModel.getEmail());
            if (user == null) {
                return error(EMAIL_NOT_EXISTS, HttpStatus.NOT_FOUND).respond();
            }
            if (otpRequestService.isUserEligibleForOtpRequest(user, resendOtpViewModel.getOtpType(), OTPSource.MOBILE)) {
                userService.generateOtp(resendOtpViewModel.getEmail(), resendOtpViewModel.getOtpType(), OTPSource.MOBILE);
                return success(OTP_SENT).respond();
            }
            return error(OTP_REQUEST_LIMIT_EXCEEDED, HttpStatus.FORBIDDEN).respond();
        } catch (CoreException ex) {
            LOG.error("Error resending otp for user!", ex);
            return error(ex.getMessage(), HttpStatus.BAD_REQUEST).respond();
        } catch (Exception ex) {
            LOG.error("Error resending otp for user!", ex);
            return error(SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR).respond();
        }
    }

    private void revokeAccessToken(String userName) {
        Collection<OAuth2AccessToken> tokensByUserName = tokenStore.findTokensByUserName(userName);
        if (tokensByUserName == null || tokensByUserName.isEmpty()) return;

        tokensByUserName.forEach(tokenStore::removeAccessToken);
        tokensByUserName.forEach((t) -> tokenStore.removeRefreshToken(t.getRefreshToken()));
    }
}
