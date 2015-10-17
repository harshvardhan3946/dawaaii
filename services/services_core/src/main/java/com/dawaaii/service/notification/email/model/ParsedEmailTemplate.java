package com.dawaaii.service.notification.email.model;

import java.io.Serializable;

public class ParsedEmailTemplate implements Serializable
{
    private static final long serialVersionUID = -6974026962875246848L;

    private String subjectContent;

    private String htmlContent;

    private String textContent;

    private final String templateName;

    public ParsedEmailTemplate(final String templateName)
    {
        this.templateName = templateName;
    }

    public String getSubjectContent()
    {
        return subjectContent;
    }

    public void setSubjectContent(final String subjectContent)
    {
        this.subjectContent = subjectContent;
    }

    public String getHtmlContent()
    {
        return htmlContent;
    }

    public void setHtmlContent(final String htmlContent)
    {
        this.htmlContent = htmlContent;
    }

    public String getTextContent()
    {
        return textContent;
    }

    public void setTextContent(final String textContent)
    {
        this.textContent = textContent;
    }

    public String getTemplateName()
    {
        return templateName;
    }
}