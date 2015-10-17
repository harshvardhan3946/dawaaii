package com.dawaaii.service.jms.impl;

/**
 * Each class implementing JMSAware must make sure that the getJmsMessageId that will be
 * returned is unique for the message. If there already exists a message with that id on the queue, it will not be send again.
 */
public interface JMSAware
{
    String getJmsMessageId();
}
