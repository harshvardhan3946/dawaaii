package com.dawaaii.scheduler.jobs.testing;

import com.dawaaii.scheduler.jobs.AbstractSpringApplicationContextStatefulJob;
import com.dawaaii.service.user.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 *
 *
 */
public class HelloWorldTestJob extends AbstractSpringApplicationContextStatefulJob {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldTestJob.class);

    @Override
    public final void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Starting " + getClass().getName());
        StopWatch stopWatchForTotalJob = new StopWatch();
        stopWatchForTotalJob.start();
        boolean enabled = Boolean.valueOf((String) jobExecutionContext.getMergedJobDataMap().get("enabled"));
        if (!enabled) {
            reportFailure(jobExecutionContext, "Job is disabled through services.properties", null);
            return;
        }

        // Hang on to the entity manager
        bindSession(jobExecutionContext);

        try {
            // Get the Spring application context
            ApplicationContext applicationContext = getApplicationContext(jobExecutionContext);
            UserService userService = (UserService) applicationContext.getBean("userService");
            long usersCount = userService.getUsersCount();
            String msg = "Hello world testing job running fine! with user count: " + usersCount;
            System.out.println(new Date() + ": " + msg);
            reportSuccess(jobExecutionContext, msg);
        } finally {
            // Release the persistent context
            try {
                unbindSession(jobExecutionContext);
            } catch (Exception ex) {
                LOG.error("Unexpected exception while releasing Entity Manager session", ex);
            }
        }
    }

}