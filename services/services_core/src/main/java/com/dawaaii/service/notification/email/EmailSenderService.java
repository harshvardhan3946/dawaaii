package com.dawaaii.service.notification.email;


import com.dawaaii.service.notification.email.model.SendEmail;

public interface EmailSenderService {
	 void sendEmail(SendEmail sendEmail);
}
