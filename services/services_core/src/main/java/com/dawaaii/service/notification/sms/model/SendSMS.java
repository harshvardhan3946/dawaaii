package com.dawaaii.service.notification.sms.model;

import java.io.Serializable;

/**
 * Created by rohit on 7/11/15.
 */
public class SendSMS implements Serializable{
    String message;
    String number;

    public SendSMS(String message, String number) {
        this.message = message;
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
