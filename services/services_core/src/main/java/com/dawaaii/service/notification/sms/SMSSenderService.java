package com.dawaaii.service.notification.sms;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.sms.model.SendSMS;
import com.dawaaii.service.user.model.User;

/**
 * Created by rohit on 7/11/15.
 */
public interface SMSSenderService {

    void sendSMS(SendSMS sendSMS);

    void sendConfirmBookingSMSToAmbulance(User user, Ambulance ambulance);

    void sendConfirmBookingSMSToUser(User user, Ambulance ambulance);
}
