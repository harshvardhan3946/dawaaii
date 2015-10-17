package com.dawaaii.web.common.model;

import javax.validation.constraints.NotNull;

import com.dawaaii.service.user.model.OTPType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class GenerateOrResendOtpViewModel {

    @Email
    @NotEmpty
    private String email;
    @NotNull
    private OTPType otpType;

    public GenerateOrResendOtpViewModel() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

	public OTPType getOtpType() {
		return otpType;
	}

	public void setOtpType(OTPType otpType) {
		this.otpType = otpType;
	}


}
