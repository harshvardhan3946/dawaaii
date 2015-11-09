package com.dawaaii.notification.email;

import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.User;
import com.dawaaii.test.AbstractDawaaiiServiceBaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rohit on 9/11/15.
 */
public class EmailServiceTest extends AbstractDawaaiiServiceBaseIntegrationTest {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Test
    public void testSendUserWelcomeEmail(){
        User user = userService.getUserByEmail("rohit.mishra0411@gmail.com");
        emailService.sendWelcomeEmail(user.getEmail(),user.getFirstName());
    }
}
