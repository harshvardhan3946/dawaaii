package com.dawaaii.service.mongo.ambulance.model;

import com.dawaaii.service.mongo.BaseDocument;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hojha on 01/01/16.
 */
@Document(collection = "ambulanceBookings")
public class AmbulanceBooking extends BaseDocument {

    private String name;
    private String email;
    private String number;
    @Indexed
    private String ambulanceId;

    public AmbulanceBooking() {
    }

    public AmbulanceBooking(String name, String email, String number, String ambulanceId) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.ambulanceId = ambulanceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmbulanceId() {
        return ambulanceId;
    }

    public void setAmbulanceId(String ambulanceId) {
        this.ambulanceId = ambulanceId;
    }
}
