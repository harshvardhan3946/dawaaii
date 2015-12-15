package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.notification.email.EmailSenderService;
import com.dawaaii.service.notification.email.model.SendEmail;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service(value = "emailSenderService")
public class EmailSenderServiceImpl implements EmailSenderService {

    private final static Logger LOG = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ActiveMQQueue mailQueue;

    @Value("${email.enabled}")
    private String mailEnabled;

    @Override
    public void sendEmail(SendEmail email) {
        jmsTemplate.send(mailQueue, session -> messageConverter.toMessage(email, session)); //produce this to email queue
    }

    public void send(SendEmail message) {
        try {
            if (!Boolean.parseBoolean(mailEnabled)) {
                LOG.error("Emails disabled on the system, not sending given message: {}", message);
                return;
            }
            SendEmail sendEmail = message;
            if (sendEmail.isSimpleMessage()) {
                SimpleMailMessage simpleMessage = createSimpleMailMessageFor(sendEmail);
                javaMailSender.send(simpleMessage);
            }
            LOG.debug("Email sending done for given message: ", message);
        } catch (Exception ex) {
            LOG.error("Error sending email: " + message, ex);
            throw ex;
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
