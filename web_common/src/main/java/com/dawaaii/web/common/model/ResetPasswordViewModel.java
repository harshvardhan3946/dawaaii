package com.dawaaii.web.common.model;

import com.dawaaii.service.user.model.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class ResetPasswordViewModel {

    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String oldPassword;

    public ResetPasswordViewModel() {
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}