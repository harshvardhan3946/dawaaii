package com.dawaaii.service.jms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQConnectionFactory extends org.apache.activemq.ActiveMQConnectionFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(ActiveMQConnectionFactory.class);

    @Override
    public void setBrokerURL(String brokerURL)
    {
        // LOG when connecting to activemq
        // using this setter to be sure it's only logged once
        // See DJ-5780
        LOG.info("ActiveMQ configured is: " + (DEFAULT_BROKER_URL.equals(brokerURL) ? "(default init setting) " : "") + brokerURL);
        LOG.info("Connecting to ActiveMQ");
        super.setBrokerURL(brokerURL);
    }
}
