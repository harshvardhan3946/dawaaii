package com.dawaaii.service.common.dao;


import java.io.Serializable;
import java.util.Date;

import com.dawaaii.service.common.model.TimeStampedAuditable;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class AuditTimeStampedInterceptor extends EmptyInterceptor
{
    protected final static Logger LOG = LoggerFactory.getLogger(AuditTimeStampedInterceptor.class);
    
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames,
            Type[] types)
    {
        return setUpdatedOn(entity, currentState, propertyNames);
    }
    
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
    {
        return setCreatedOn(entity, state, propertyNames);
    }

    private boolean setUpdatedOn(Object entity, Object[] currentState, String[] propertyNames)
    {
        return setField(entity, currentState, propertyNames, "updatedOn");
    }

    private boolean setCreatedOn(Object entity, Object[] currentState, String[] propertyNames)
    {
        return setField(entity, currentState, propertyNames, "createdOn");
    }

    private boolean setField(Object entity, Object[] currentState, String[] propertyNames, String fieldName) {
        if (entity instanceof TimeStampedAuditable)
        {
            for ( int i=0; i < propertyNames.length; i++ )
            {
                if ( fieldName.equals(propertyNames[i]) ) {
                    currentState[i] = new Date();
                    return true;
                }
            }
        }
        return false;
    }
}
