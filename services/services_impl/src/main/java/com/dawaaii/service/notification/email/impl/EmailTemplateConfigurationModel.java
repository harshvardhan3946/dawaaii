package com.dawaaii.service.notification.email.impl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "emailtemplateconfiguration")
public class EmailTemplateConfigurationModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private List<EmailTemplateIncludeDeclarationModel> includeDeclarations;

    public EmailTemplateConfigurationModel()
    {
        this.includeDeclarations = new ArrayList<>();
    }

    @XmlElementWrapper(name = "includes")
    @XmlElement(name = "include")
    public List<EmailTemplateIncludeDeclarationModel> getIncludeDeclarations()
    {
        return includeDeclarations;
    }

    public void setIncludeDeclarations(List<EmailTemplateIncludeDeclarationModel> inludeDeclarations)
    {
        this.includeDeclarations = inludeDeclarations;
    }
}
