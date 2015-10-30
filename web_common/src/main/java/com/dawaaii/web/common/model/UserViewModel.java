package com.dawaaii.web.common.model;

import com.dawaaii.service.user.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserViewModel {

    private Long id;
    private String userName;
    private String initials;
    @NotEmpty
    @Email
    private String email;
    private Character gender;
    private boolean active;
    @NotEmpty
    @Size(max = 100)
    @Pattern(regexp = "[a-zA-Z .]*")
    private String lastName;
    @NotEmpty
    @Size(max = 100)
    @Pattern(regexp = "[a-zA-Z .]*")
    private String firstName;
    @NotEmpty
    private String password;

    private String addressLine1;
    private String addressLine2;
    private String phoneNumber;
    private String city;
    private String zipPostalCode;
    private String state;
    private boolean emailConfirmed;

    public UserViewModel() {
    }

    public UserViewModel(User user) {
        id = user.getId();
        userName = user.getUserName();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        initials = user.getInitials();
        gender = user.getGender();
        addressLine1 = user.getAddressLine1();
        addressLine2 = user.getAddressLine2();
        phoneNumber = user.getPhoneNumber();
        city = user.getCity();
        zipPostalCode = user.getZipPostalCode();
        state = user.getState();
        emailConfirmed = user.isEmailConfirmed();
    }

    public User toUserEntity() {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setInitials(initials);
        user.setGender(gender);
        return user;
    }

    public User udpateProfile(User user) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddressLine1(addressLine1);
        user.setAddressLine2(addressLine2);
        user.setPhoneNumber(phoneNumber);
        user.setCity(city);
        user.setZipPostalCode(zipPostalCode);
        user.setState(state);
        return user;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getInitials() {
        return initials;
    }


    public String getEmail() {
        return email;
    }

    public Character getGender() {
        return gender;
    }

    public boolean isActive() {
        return active;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipPostalCode() {
        return zipPostalCode;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }
}