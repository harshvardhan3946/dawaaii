package com.dawaaii.web.common.model;

import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.mongo.ambulance.model.AmbulanceType;

import static com.dawaaii.service.mongo.ambulance.model.AmbulanceType.WITH_OXYGEN_CYLINDER;

/**
 * Created by hojha on 19/11/15.
 */
public class AmbulanceViewModel {

    private String id;
    private String serviceProviderName;
    private String description;
    private String city;
    private String state;
    private String area;
    private String address;
    private String contactNumber;
    private String mobileNumber;
    private String email;
    private double latitude;
    private double longitude;
    private String price;
    private String ambulanceType;

    public AmbulanceViewModel() {
    }

    public AmbulanceViewModel(Ambulance ambulance) {
        this.setId(ambulance.getId());
        this.setServiceProviderName(ambulance.getServiceProviderName());
        this.setDescription(ambulance.getDescription());
        this.setCity(ambulance.getCity());
        this.setState(ambulance.getState());
        this.setArea(ambulance.getArea());
        this.setAddress(ambulance.getAddress());
        this.setContactNumber(ambulance.getContactNumber());
        this.setMobileNumber(ambulance.getContactNumber());
        this.setEmail(ambulance.getEmail());
        this.setLatitude(ambulance.getPoint().getX());
        this.setLongitude(ambulance.getPoint().getY());
        this.setPrice(ambulance.getPrice() == null ? "NA" : ambulance.getPrice().toString());
        this.setAmbulanceType(getAmbulanceTypeName(ambulance.getAmbulanceType()));
    }

    private String getAmbulanceTypeName(AmbulanceType ambulanceType) {
        if (WITH_OXYGEN_CYLINDER == ambulanceType) {
            return "Ambulance with Oxygen.";
        }
        return "Normal Ambulance.";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmbulanceType() {
        return ambulanceType;
    }

    public void setAmbulanceType(String ambulanceType) {
        this.ambulanceType = ambulanceType;
    }
}
