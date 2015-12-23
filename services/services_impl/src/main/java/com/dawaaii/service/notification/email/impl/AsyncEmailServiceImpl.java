package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.jms.impl.SimpleMessageProducer;
import com.dawaaii.service.mongo.ambulance.model.Ambulance;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.email.model.SendEmail;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.core.JmsTemplate;
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

    @Override
    public void sendUserOTPEmail(User user, UserOTP userOTP) {

    }

    @Override
    public void sendWelcomeEmail(String email, String firstName) {
        try {
            String subject = "Welcome to dawaaii.in";
            String body = "<h3>Dear " + firstName + ",</h3><p>This email is intended to welcome you at dawaaii.in.</p><br><b>Cheers,<br>Team Dawaaii</b>";
            sendEmail(from, "INFO", email, subject, null, body);
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
        emailMessage.setSimpleMessage(false);

        jmsTemplate.send(mailQueue, session -> messageConverter.toMessage(emailMessage, session)); //produce this to email queue
    }

    @Override
    public void sendConfirmBookingEmailToUser(String userEmail, String userName, Ambulance ambulance) {
        try {
            String subject = "Ambulance booking at dawaaii.in";
            String body = "<h3>Dear " + userName + ",</h3><div><p>This email" +
                    " is a confirmation mail to confirm about an ambulance booking" +
                    " that you have made with following details</p><br>" +
                    "<table border='1' padding='5px' style='border-collapse:collapse'><th colspan='2'>Ambulance details</th>" +
                    "<tr><td>Ambulance address</td><td>" + ambulance.getAddress() +
                    "</td></tr><td>Ambulance contact number</td><td>" + ambulance.getMobileNumber() +
                    "</td></tr></table></div><br><br><b>Cheers,<br>Team Dawaaii</b>";
            sendEmail(from, "INFO", userEmail, subject, null, body);

        } catch (Exception ex) {
            LOG.error("error while sending confirm booking email to user with email " + userEmail, ex);
        }
    }

    @Override
    public void sendConfirmBookingEmailToAmbulance(String userEmail, String userName, String userNumber, Ambulance ambulance) {
        try {
            String subject = "Ambulance booking at dawaaii.in";
            String body = "<h3>Dear " + ambulance.getServiceProviderName() + ",</h3><div><p>This email" +
                    " is a confirmation mail to confirm about your ambulance booking at dawaaii.in by</p><br>" +
                    "<table border='1' padding='5px' style='border-collapse:collapse'><th colspan='2'>User Details</th>" +
                    "<tr><td>Name</td><td>" + userName +
                    "</td><tr><td>Email</td><td>" + userEmail +
                    "</td><tr><td>Contact number</td><td>" + userNumber +
                    "</td></tr></table></div><br><br><b>Cheers,<br>Team Dawaaii</b>";
            sendEmail(from, "INFO", ambulance.getEmail(), subject, null, body);

        } catch (Exception ex) {
            LOG.error("error while sending confirm booking email to ambulance" + ambulance.getServiceProviderName(), ex);
        }
    }

    public void send(SendEmail sendEmail) {
        System.out.println("sending email "+sendEmail.isSimpleMessage());
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
            System.out.println("error "+ex.getMessage());
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