package com.dawaaii.service.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dawaaii.service.common.model.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "userotp")
public class UserOTP extends BaseEntity {
    @Column(name = "otp", nullable = false)
    private String otp;

	@Column(name = "expireson", nullable = false)
    private Date expiresOn;

    @Column(name = "used", nullable = false)
    private boolean used;

    @Column(name = "usedon")
    private Date usedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "otptype", nullable = false)
    private OTPType otpType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "otpsource", nullable = false)
    private OTPSource otpSource;
    
    @Column(name = "uuid", nullable = false)
    private String otpUniqueCode;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    public String getOTP() {
        return otp;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public void setOTP(String OTP) {
        this.otp = OTP;
    }


    public OTPType getOtpType() {
        return otpType;
    }

    public void setOtpType(OTPType otpType) {
        this.otpType = otpType;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Date getUsedOn() {
        return usedOn;
    }

    public void setUsedOn(Date usedOn) {
        this.usedOn = usedOn;
    }
    
    public String getOtpUniqueCode() {
		return otpUniqueCode;
	}

	public void setOtpUniqueCode(String otpUniqueCode) {
		this.otpUniqueCode = otpUniqueCode;
	}

	public OTPSource getOtpSource() {
		return otpSource;
	}

	public void setOtpSource(OTPSource otpSource) {
		this.otpSource = otpSource;
	}
}
