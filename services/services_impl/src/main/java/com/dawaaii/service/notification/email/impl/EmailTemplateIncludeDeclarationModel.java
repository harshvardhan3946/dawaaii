package com.dawaaii.service.notification.email.impl;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class EmailTemplateIncludeDeclarationModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String templateName;

    private String templateRootFolder;

    public EmailTemplateIncludeDeclarationModel() {
    }

    @XmlElement(name = "templatename")
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @XmlElement(name = "templaterootfolder")
    public String getTemplateRootFolder() {
        return templateRootFolder;
    }

    public void setTemplateRootFolder(String templatePath) {
        this.templateRootFolder = templatePath;
    }
}
