package com.dawaaii.service.notification.email;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;


public interface EmailService {

    void sendUserOTPEmail(User user, UserOTP userOTP);

    void sendWelcomeEmail(String email, String firstName);

    void sendEmail(String fromAddress, String string, String emailAddress, String subject, String bodyText, String bodyHtml);

    void sendConfirmBookingEmailToAmbulance(Ambulance ambulance);

    void sendConfirmBookingEmailToUser(User user);

}