package com.dawaaii.service.notification.email.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SendEmail implements Serializable
{
	private String fromAddress;
	private String fromName;
	private String toAddress;
	private String subject;
	private String bodyText;
	private String bodyHtml;
    private String attachments[];
    private boolean simpleMessage;
	
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBodyText() {
		return bodyText;
	}
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	public String getBodyHtml() {
		return bodyHtml;
	}
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}
	public boolean isSimpleMessage() {
		return simpleMessage;
	}
	public void setSimpleMessage(boolean simpleMessage) {
		this.simpleMessage = simpleMessage;
	}
    public String[] getAttachments() {
        return attachments;
    }
    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fromName", fromName)
                .append("toAddress", toAddress)
                .append("subject", subject)
                .append("bodyText", bodyText)
                .append("bodyHtml", bodyHtml)
                .append("simpleMessage", simpleMessage)
                .build()
                ;
    }
    
}
