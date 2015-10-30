package com.dawaaii.service.user.impl;

import com.dawaaii.service.auth.impl.TokenGenerator;
import com.dawaaii.service.dao.jpa.UserOneTimePasswordRepository;
import com.dawaaii.service.dao.jpa.UserRepository;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.otp.OTPRequestService;
import com.dawaaii.service.security.DawaaiiRoleService;
import com.dawaaii.service.security.model.Role;
import com.dawaaii.service.security.model.RoleCode;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by rohit on 29/10/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserServiceImpl.class})
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserOneTimePasswordRepository userOneTimePasswordRepository;
    @Mock
    private EmailService asyncMailService;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private DawaaiiRoleService dawaaiiRoleService;
    @Mock
    private OTPRequestService otpRequestService;

    private UserService userService;

    @Before
    public void setUp() throws Exception{
        userService = new UserServiceImpl(userRepository,userOneTimePasswordRepository,asyncMailService,tokenGenerator,dawaaiiRoleService,otpRequestService);
    }
    @Test
    public void shouldSaveAValidUser() throws Exception {
        User user = new User();
        user.setCreationSource(UserCreationSource.WEBSITE);
        when(userRepository.save(user)).thenReturn(user);
        UserOTP userOTP = new UserOTP();
        userOTP.setOTP("test");
        when(tokenGenerator.generateOTP(user, OTPType.CONFIRM_EMAIL, OTPSource.WEBSITE)).thenReturn(userOTP);
        when(dawaaiiRoleService.getRoleByCode(RoleCode.WEBSITE_APP_GENERAL_USER)).thenReturn(new Role());
        user.setEmail("rohit.mishra0411@gmail.com");
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }
}
