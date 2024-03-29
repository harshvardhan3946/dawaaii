package com.dawaaii.service.notification.sms;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.sms.model.SendSMS;

/**
 * Created by rohit on 7/11/15.
 */
public interface SMSSenderService {

    void sendSMS(SendSMS sendSMS);

    void sendConfirmBookingSMSToAmbulance(String userName, String userNumber, Ambulance ambulance);

    void sendConfirmBookingSMSToUser(String userEmail, String userNumber, Ambulance ambulance);
}
