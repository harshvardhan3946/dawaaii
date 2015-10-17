package com.dawaaii.service.user.model;

import com.dawaaii.service.common.model.BaseEntity;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "otprequestcount")
public class OTPRequestCount extends BaseEntity{

    @Column(name = "otptype", nullable = false)
    @Enumerated(EnumType.STRING)
    private OTPType otpType;
    
    @Column(name = "otpsource", nullable = false)
    @Enumerated(EnumType.STRING)
    private OTPSource otpSource;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Column(name = "requestcount", nullable = false)
    private int requestCount;

    public OTPRequestCount() {
    }

    public OTPType getOtpType() {
        return otpType;
    }

    public void setOtpType(OTPType otpType) {
        this.otpType = otpType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

	public OTPSource getOtpSource() {
		return otpSource;
	}

	public void setOtpSource(OTPSource otpSource) {
		this.otpSource = otpSource;
	}
}
