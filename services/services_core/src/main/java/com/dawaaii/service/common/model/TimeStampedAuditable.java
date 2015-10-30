package com.dawaaii.service.common.model;

import java.util.Date;

public interface TimeStampedAuditable
{
	Date getCreatedOn();
    void setCreatedOn(Date createdOn);
    Date getUpdatedOn();
    void setUpdatedOn(Date updatedOn);
}
