package com.dawaaii.web.common.model;

import com.dawaaii.service.user.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ForgotPasswordViewModel {

    @Email
    private String email;
    @NotEmpty
    private String password;
    private String otp;

    public ForgotPasswordViewModel() {
    }

    public User toUserEntity() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
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
