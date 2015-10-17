package com.dawaaii.service.notification.email.model;

public class WelcomeEmailTemplateModel implements EmailTemplateModel
{
    private static final long serialVersionUID = 1L;

    private String url;

    private String name;

    private String email;

    @Override
    public String getUrl() {
        return this.url;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
