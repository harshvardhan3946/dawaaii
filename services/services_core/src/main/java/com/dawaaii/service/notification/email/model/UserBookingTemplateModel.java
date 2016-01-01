package com.dawaaii.service.notification.email.model;

/**
 * Created by rohit on 29/12/15.
 */
public class UserBookingTemplateModel implements EmailTemplateModel {

    private static final long serialVersionUID = 1L;

    private String url;

    private String name;

    private String addressLine;

    private String bookingId;

    private String price;

    private String phoneNumber;

    //private String ambulanceMobileNumber;

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
