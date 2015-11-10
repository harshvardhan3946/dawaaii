package com.dawaaii.service.notification.sms.impl;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.sms.SMSSenderService;
import com.dawaaii.service.notification.sms.model.SendSMS;
import com.dawaaii.service.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by rohit on 7/11/15.
 */
@Service(value = "asyncSMSSenderServiceImpl")
public class AsyncSMSSenderServiceImpl implements SMSSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSMSSenderServiceImpl.class);
    @Value("${sms.base.url}")
    private String smsBaseUrl;
    @Value("${sms.username}")
    private String username;
    @Value("${sms.password}")
    private String password;

    @Override
    public void sendSMS(SendSMS sendSMS) {
        RestTemplate restTemplate = new RestTemplate();
        StringBuilder request = new StringBuilder();
        request.append(smsBaseUrl)
                .append("uname=")
                .append(username)
                .append("&password=")
                .append(password)
                .append("&sender=alerts&receiver=")
                .append(sendSMS.getNumber())
                .append("&route=T&msgtype=1&sms=")
                .append(sendSMS.getMessage());
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(request.toString(), String.class);
        if(OK == stringResponseEntity.getStatusCode()){
            LOGGER.debug(stringResponseEntity.toString());
        }
        else{
            LOGGER.debug("Error sending sms for request",request);
        }
   }

    @Override
    public void sendConfirmBookingSMSToAmbulance(User user, Ambulance ambulance) {
        //TODO implement this
    }

    @Override
    public void sendConfirmBookingSMSToUser(Ambulance ambulance, User user) {
        //TODO implement this
    }
}
