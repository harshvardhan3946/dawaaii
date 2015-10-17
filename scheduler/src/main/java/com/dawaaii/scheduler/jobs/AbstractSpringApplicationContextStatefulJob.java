package com.dawaaii.scheduler.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * Contains an uniform approach for retrieving the Spring application context. <br />
 * Usable by Jobs extending this class <br />
 * 
 * Based on: http://cse-mjmcl.cse.bris.ac.uk/blog/2007/06/20/1182370280435.html <br />
 * <br />
 * 
 * <b>a StatefulJob will never be started concurrently</b>
 * 
 * 
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class AbstractSpringApplicationContextStatefulJob extends AbstractSpringApplicationContextJob
{
}
