package com.dawaaii.service.jms.impl;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;

public class TimedJmsTemplate extends JmsTemplate
{
    private static final Logger LOG = LoggerFactory.getLogger(TimedJmsTemplate.class);

    @Override
    public void send(Destination destination, MessageCreator messageCreator) throws JmsException
    {

        StopWatch sw = null;
        if (LOG.isDebugEnabled())
        {
            sw = new StopWatch();
            sw.start();
        }
        super.send(destination, messageCreator);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Sending JMS message took {}ms destination: {}",new Object[] { sw.getTime(), ((ActiveMQDestination) destination).getQualifiedName() });
        }
    }

    @Override
    public void send(MessageCreator messageCreator) throws JmsException
    {
        StopWatch sw = null;
        if (LOG.isDebugEnabled())
        {
            sw = new StopWatch();
            sw.start();
        }
        super.send(messageCreator);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Sending JMS message took {}ms", sw.getTime());
        }
    }

    @Override
    public void send(String destinationName, MessageCreator messageCreator) throws JmsException
    {
        StopWatch sw = null;
        if (LOG.isDebugEnabled())
        {
            sw = new StopWatch();
            sw.start();
        }
        super.send(destinationName, messageCreator);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Sending JMS message took {}ms destination: {}", new Object[] { sw.getTime(), destinationName });
        }
    }
}
