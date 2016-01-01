package com.dawaaii.service.notification.email.model;

/**
 * Created by rohit on 29/12/15.
 */
public class UserBookingTemplateModel implements EmailTemplateModel {

    private static final long serialVersionUID = 1L;

    private String url;

    private String userName;

    private String ambulanceAddress;

    private String ambulanceMobileNumber;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAmbulanceAddress() {
        return ambulanceAddress;
    }

    public void setAmbulanceAddress(String ambulanceAddress) {
        this.ambulanceAddress = ambulanceAddress;
    }

    public String getAmbulanceMobileNumber() {
        return ambulanceMobileNumber;
    }

    public void setAmbulanceMobileNumber(String ambulanceMobileNumber) {
        this.ambulanceMobileNumber = ambulanceMobileNumber;
    }
}
