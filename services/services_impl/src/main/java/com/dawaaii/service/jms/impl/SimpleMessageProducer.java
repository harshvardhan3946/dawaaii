package com.dawaaii.service.jms.impl;


import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.*;
import java.util.Enumeration;

/**
 *	
 */
public class SimpleMessageProducer
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessageProducer.class);

    protected JmsTemplate jmsTemplate;

    protected Destination consumerDestination;

    protected Destination producerDestination;

    protected MessageConverter messageConverter;

    protected void send(Destination consumerDestination, final Object wrapperForJMS)
    {
        jmsTemplate.send(consumerDestination, new MessageCreator()
        {
            public Message createMessage(final Session session) throws JMSException
            {
                return messageConverter.toMessage(wrapperForJMS, session);
            }
        });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected boolean existingMessage(final JMSAware jmsMessage)
    {
        final String jmsMessageId = jmsMessage.getJmsMessageId();
        String queueName = consumerDestination.toString();
        final String destination;
        if (queueName.startsWith("queue://"))
        {
            destination = queueName.substring(8);
        }
        else
        {
            destination = queueName;
        }
        return (Boolean) jmsTemplate.browse(destination, new BrowserCallback()
        {
            public Object doInJms(Session session, QueueBrowser browser) throws JMSException
            {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                Enumeration enumeration = browser.getEnumeration();
                while (enumeration.hasMoreElements())
                {
                    Message message = (Message) enumeration.nextElement();
                    String id = message.getStringProperty("id");
                    if (jmsMessageId.equals(id))
                    {
                        LOG.debug("Existing message with id '{}' was found on queue '{}', searching took: {}ms", new Object[] {
                                jmsMessageId, destination, stopWatch.getTime() });
                        return Boolean.TRUE;
                    }
                }
                LOG.debug("Existing message with id '{}' was not found on queue '{}', total searchtime: {}ms", new Object[] { jmsMessageId,
                        destination, stopWatch.getTime() });
                return Boolean.FALSE;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }

    public void setConsumerDestination(Destination consumerDestination)
    {
        this.consumerDestination = consumerDestination;
    }

    public void setProducerDestination(Destination producerDestination)
    {
        this.producerDestination = producerDestination;
    }

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}
    
}
