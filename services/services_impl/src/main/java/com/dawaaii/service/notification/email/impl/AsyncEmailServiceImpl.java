package com.dawaaii.service.notification.email.impl;

import com.dawaaii.service.jms.impl.SimpleMessageProducer;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.email.EmailService;
import com.dawaaii.service.notification.email.model.SendEmail;
import com.dawaaii.service.user.model.User;
import com.dawaaii.service.user.model.UserOTP;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

@Service(value = "asyncMailService")
public class AsyncEmailServiceImpl extends SimpleMessageProducer implements EmailService {

    private final static Logger LOG = LoggerFactory.getLogger(AsyncEmailServiceImpl.class);

    private final JmsTemplate jmsTemplate;
    private final MessageConverter messageConverter;
    private final ActiveMQQueue mailQueue;

    @Autowired
    public AsyncEmailServiceImpl(JmsTemplate jmsTemplate, MessageConverter messageConverter, ActiveMQQueue mailQueue) {
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
        this.mailQueue = mailQueue;
    }

    private void send(SendEmail mail) {
        LOG.debug("Sending email message to queue: {}", mail);
        MessageCreator messageCreator = session ->
                messageConverter.toMessage(mail, session);
        jmsTemplate.send(mailQueue, messageCreator);

        LOG.debug("Sending email message to queue done!");
    }

    @Override
    public void sendUserOTPEmail(User user, UserOTP userOTP) {

    }
}