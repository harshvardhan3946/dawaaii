package com.dawaaii.service.notification.sms.impl;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.sms.SMSSenderService;
import com.dawaaii.service.notification.sms.model.SendSMS;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by rohit on 7/11/15.
 */
@Service(value = "smsSenderServiceImpl")
public class SMSSenderServiceImpl implements SMSSenderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSSenderServiceImpl.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ActiveMQQueue smsQueue;

    @Value("${sms.base.url}")
    private String smsBaseUrl;

    @Value("${sms.username}")
    private String username;

    @Value("${sms.password}")
    private String password;

    @Override
    public void sendSMS(SendSMS sms) {
        jmsTemplate.send(smsQueue, session -> messageConverter.toMessage(sms, session)); //produce this sms to sms queue
    }

    public void send(SendSMS sendSMS) {
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
        if (OK == stringResponseEntity.getStatusCode()) {
            LOGGER.debug(stringResponseEntity.toString());
        } else {
            LOGGER.debug("Error sending sms for request", request);
        }
    }

    @Override
    public void sendConfirmBookingSMSToAmbulance(String userEmail, String userNumber, Ambulance ambulance) {
        SendSMS sendSMS = new SendSMS("ambulance booked by " + userEmail + ":: contact number::" + userNumber + " for your ambulance", ambulance.getMobileNumber());
        sendSMS(sendSMS);
    }

    @Override
    public void sendConfirmBookingSMSToUser(String userName, String userNumber, Ambulance ambulance) {
        SendSMS sendSMS = new SendSMS("Hi " + userName + " You have booked an ambulance :: contact number::" + ambulance.getMobileNumber() + ":: address::" + ambulance.getAddress(), userNumber);
        sendSMS(sendSMS);
    }
}
