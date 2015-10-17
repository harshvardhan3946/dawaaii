package com.dawaaii.service.jms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

public class JmsExceptionListenerImpl implements ExceptionListener
{
    private static final Logger LOG = LoggerFactory.getLogger(JmsExceptionListenerImpl.class);
    
    private ExceptionListener exceptionListener;
    
    public JmsExceptionListenerImpl()
    {
    }
    
    public JmsExceptionListenerImpl(Connection connection, ExceptionListener exceptionListener)
    {
        this();
        this.exceptionListener = exceptionListener;
        LOG.debug("Created exception listener on connection {} with chained exception listener {}",connection,exceptionListener);
    }
    
    @Override
    public void onException(JMSException ex)
    {
        LOG.error("An exception occurred in JMS: ",ex);
        if (ex.getLinkedException() != null)
        {
            LOG.error("Linked JMS exception: ",ex.getLinkedException());
        }
        
        if (exceptionListener != null) 
        {
            try
            {
                exceptionListener.onException(ex);
            }
            catch(Exception e)
            {
                LOG.error("An error occurred invoking the nested exception handler: ",e);
            }
        }
    }

    public void setExceptionListener(ExceptionListener exceptionListener)
    {
        this.exceptionListener = exceptionListener;
    }
}