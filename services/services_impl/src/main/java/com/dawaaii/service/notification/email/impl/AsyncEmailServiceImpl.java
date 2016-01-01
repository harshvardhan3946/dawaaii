package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.jms.impl.SimpleMessageProducer;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.email.EmailTemplateService;
import com.dawaaii.service.notification.email.model.*;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service(value = "asyncMailService")
public class AsyncEmailServiceImpl extends SimpleMessageProducer implements EmailService {

    private final static Logger LOG = LoggerFactory.getLogger(AsyncEmailServiceImpl.class);

    @Value("${email.from}")
    private String from;
    @Value("${application.web.url}")
    private String webUrl;

    @Autowired
    private JavaMailSender javaMailSender;

    private final JmsTemplate jmsTemplate;
    private final MessageConverter messageConverter;
    private final ActiveMQQueue mailQueue;
    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    public AsyncEmailServiceImpl(JmsTemplate jmsTemplate, MessageConverter messageConverter, ActiveMQQueue mailQueue) {
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
        this.mailQueue = mailQueue;
    }

    @Override
    public void sendUserOTPEmail(User user, UserOTP userOTP) {

    }

    @Override
    public void sendWelcomeEmail(String email, String firstName) {
        SendEmail emailMessage = new SendEmail();
        WelcomeEmailTemplateModel welcomeEmailTemplateModel = new WelcomeEmailTemplateModel();
        welcomeEmailTemplateModel.setEmail(email);
        welcomeEmailTemplateModel.setName(firstName);
        welcomeEmailTemplateModel.setUrl(webUrl);

        ParsedEmailTemplate parsedEmailTemplate;
        parsedEmailTemplate = emailTemplateService.loadparsedEmailTemplate(EmailTemplateType.WELCOME_EMAIL, welcomeEmailTemplateModel);
        emailMessage.setBodyHtml(parsedEmailTemplate.getHtmlContent());
        emailMessage.setToAddress(email);
        emailMessage.setSubject(parsedEmailTemplate.getSubjectContent());
        emailMessage.setFromAddress(from);
        emailMessage.setFromName("INFO");
        emailMessage.setSimpleMessage(false);
        MessageCreator messageCreator = session ->
                messageConverter.toMessage(emailMessage, session);
        jmsTemplate.send(mailQueue, messageCreator);
    }


    @Override
    public void sendEmail(String fromAddress, String fromName, String emailAddress, String subject, String bodyText, String bodyHtml) {
        SendEmail emailMessage = new SendEmail();
        emailMessage.setFromAddress(fromAddress);
        emailMessage.setFromName(fromName);
        emailMessage.setToAddress(emailAddress);
        emailMessage.setSubject(subject);
        emailMessage.setBodyText(bodyText);
        emailMessage.setBodyHtml(bodyHtml);
        emailMessage.setSimpleMessage(false);

        jmsTemplate.send(mailQueue, session -> messageConverter.toMessage(emailMessage, session)); //produce this to email queue
    }

    @Override
    public void sendConfirmBookingEmailToUser(String userEmail, String userName, String userNumber, String userAddress,  Ambulance ambulance) {
            SendEmail emailMessage = new SendEmail();
            UserBookingTemplateModel userBookingTemplateModel = new UserBookingTemplateModel();
            userBookingTemplateModel.setUrl(webUrl);
            userBookingTemplateModel.setName(userName);
            userBookingTemplateModel.setAddressLine(userAddress);
            userBookingTemplateModel.setBookingId("");
            userBookingTemplateModel.setPrice(ambulance.getPrice() != null ? ambulance.getPrice().toString() : "");
            userBookingTemplateModel.setPhoneNumber(userNumber);

            ParsedEmailTemplate parsedEmailTemplate;
            parsedEmailTemplate = emailTemplateService.loadparsedEmailTemplate(EmailTemplateType.USER_BOOKING_EMAIL, userBookingTemplateModel);

            emailMessage.setBodyHtml(parsedEmailTemplate.getHtmlContent());
            emailMessage.setToAddress(userEmail);
            emailMessage.setSubject(parsedEmailTemplate.getSubjectContent());
            emailMessage.setFromAddress(from);
            emailMessage.setFromName("INFO");
            emailMessage.setSimpleMessage(false);
            MessageCreator messageCreator = session ->
                    messageConverter.toMessage(emailMessage, session);
            jmsTemplate.send(mailQueue, messageCreator);
    }

    @Override
    public void sendConfirmBookingEmailToAmbulance(String userEmail, String userName, String userNumber, String userAddress, Ambulance ambulance) {
        SendEmail emailMessage = new SendEmail();
        AmbulanceBookingTemplateModel ambulanceBookingTemplateModel = new AmbulanceBookingTemplateModel();
        ambulanceBookingTemplateModel.setUrl(webUrl);
        ambulanceBookingTemplateModel.setName(userName);
        ambulanceBookingTemplateModel.setAddressLine(userAddress);
        ambulanceBookingTemplateModel.setBookingId("");
        ambulanceBookingTemplateModel.setPrice(ambulance.getPrice() != null ? ambulance.getPrice().toString() : "");
        ambulanceBookingTemplateModel.setPhoneNumber(userNumber);

        ParsedEmailTemplate parsedEmailTemplate;
        parsedEmailTemplate = emailTemplateService.loadparsedEmailTemplate(EmailTemplateType.AMBULANCE_BOOKING_TEMPLATE, ambulanceBookingTemplateModel);

        emailMessage.setBodyHtml(parsedEmailTemplate.getHtmlContent());
        emailMessage.setToAddress(userEmail);
        emailMessage.setSubject(parsedEmailTemplate.getSubjectContent());
        emailMessage.setFromAddress(from);
        emailMessage.setFromName("INFO");
        emailMessage.setSimpleMessage(false);
        MessageCreator messageCreator = session ->
                messageConverter.toMessage(emailMessage, session);
        jmsTemplate.send(mailQueue, messageCreator);
    }

    public void send(SendEmail sendEmail) {
        LOG.info("sending email ",sendEmail.isSimpleMessage());
        try {
            if (sendEmail.isSimpleMessage()) {
                SimpleMailMessage simpleMessage = createSimpleMailMessageFor(sendEmail);
                javaMailSender.send(simpleMessage);
            }
            else {
                MimeMessagePreparator preparator = mimeMessage -> {
                    MimeMessageHelper message;
                    if(sendEmail.getAttachments() != null && sendEmail.getAttachments().length > 0) {
                        message = new MimeMessageHelper(mimeMessage , true);
                        for(String attachment:sendEmail.getAttachments()) {
                                FileSystemResource file = new FileSystemResource(attachment);
                                message.addAttachment(file.getFilename(), file);
                        }
                    }
                    else{
                        message = new MimeMessageHelper(mimeMessage);
                    }
                    message.setTo(sendEmail.getToAddress());
                    message.setSubject(sendEmail.getSubject());
                    message.setText(sendEmail.getBodyHtml(), true);
                    message.setFrom(sendEmail.getFromAddress());
                };
                javaMailSender.send(preparator);
            }
            LOG.debug("Email sending done for given message: ", sendEmail);
        } catch (Exception ex) {
            LOG.error("Error sending email: " + sendEmail, ex);
        }
    }

    private SimpleMailMessage createSimpleMailMessageFor(SendEmail sendEmail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(sendEmail.getToAddress());
        simpleMailMessage.setSubject(sendEmail.getSubject());
        simpleMailMessage.setFrom(sendEmail.getFromAddress());
        simpleMailMessage.setText(sendEmail.getBodyText());
        return simpleMailMessage;
    }

}