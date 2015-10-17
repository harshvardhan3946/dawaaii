package com.dawaaii.web.common.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfirmEmailViewModel {

    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String otp;

    public ConfirmEmailViewModel() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
