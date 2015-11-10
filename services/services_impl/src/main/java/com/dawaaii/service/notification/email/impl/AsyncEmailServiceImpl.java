package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.jms.impl.SimpleMessageProducer;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.email.model.SendEmail;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service(value = "asyncMailService")
public class AsyncEmailServiceImpl extends SimpleMessageProducer implements EmailService {

    private final static Logger LOG = LoggerFactory.getLogger(AsyncEmailServiceImpl.class);

    @Value("${email.from}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    private final JmsTemplate jmsTemplate;
    private final MessageConverter messageConverter;
    private final ActiveMQQueue mailQueue;

    @Autowired
    public AsyncEmailServiceImpl(JmsTemplate jmsTemplate, MessageConverter messageConverter, ActiveMQQueue mailQueue) {
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
        this.mailQueue = mailQueue;
    }

/*
    private void send(SendEmail mail) {
        LOG.debug("Sending email message to queue: {}", mail);
        MessageCreator messageCreator = session ->
                messageConverter.toMessage(mail, session);
        //jmsTemplate.send(mailQueue, messageCreator);

        LOG.debug("Sending email message to queue done!");
    }
*/

    @Override
    public void sendUserOTPEmail(User user, UserOTP userOTP) {

    }

    @Override
    public void sendWelcomeEmail(String email, String firstName) {
        try {
            String subject = "Welcome to dawaaii.in";
            String body = "Dear " + firstName + ",\n\nThis email is intended to welcome you at dawaaii.in.\n\nCheers\nTeam Dawaaii";
            sendEmail(from, "INFO", email, subject, body, null);

        } catch (Exception ex) {
            LOG.error("error while sending welcome email", ex);
        }
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
        emailMessage.setSimpleMessage(true);

        send(emailMessage);
    }

    @Override
    public void sendConfirmBookingEmailToAmbulance(Ambulance ambulance) {
        //TODO implement this
    }

    @Override
    public void sendConfirmBookingEmailToUser(User user) {
        //TODO implement this
    }

    private void send(SendEmail message) {
        try {
            if (message.isSimpleMessage()) {
                SimpleMailMessage simpleMessage = createSimpleMailMessageFor(message);
                javaMailSender.send(simpleMessage);
            }
            LOG.debug("Email sending done for given message: ", message);
        } catch (Exception ex) {
            LOG.error("Error sending email: " + message, ex);
            //throw ex;
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