package com.dawaaii.notification.sms;

import com.dawaaii.service.notification.sms.SMSSenderService;
import com.dawaaii.service.notification.sms.model.SendSMS;
import com.dawaaii.service.user.UserService;
import com.dawaaii.service.user.model.User;
import com.dawaaii.test.AbstractDawaaiiServiceBaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rohit on 9/11/15.
 */
public class SMSServiceTest extends AbstractDawaaiiServiceBaseIntegrationTest {
    @Autowired
    private SMSSenderService smsSenderService;
    @Autowired
    private UserService userService;

    @Test
    public void testSendSMS(){
        User user = userService.getUserByEmail("rohit.mishra0411@gmail.com");
        String number = user.getPhoneNumber();
        String message = "this is a test message for integration test";
        SendSMS sendSMS = new SendSMS(message,number);
        smsSenderService.sendSMS(sendSMS);
    }
}
