package com.dawaaii.scheduler.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Contains an uniform approach for retrieving the Spring application context. <br />
 * Usable by Jobs extending this class <br />
 * <p/>
 * <b>a Job can be started concurrently!</b>
 *
 * @see AbstractSpringApplicationContextStatefulJob
 */
public abstract class AbstractSpringApplicationContextJob extends QuartzJobBean //implements Job
{
    public static final String SCHEDULER_REPORT_EMAILADDRESS_DATAMAPKEY = "scheduler.report.emailaddress";

    private static final String APPLICATION_CONTEXT_KEY = "applicationContext";

    private Logger LOG = LoggerFactory.getLogger(AbstractSpringApplicationContextJob.class);

    protected final ApplicationContext getApplicationContext(JobExecutionContext context) throws JobExecutionException {
        ApplicationContext appCtx = null;
        try {
            appCtx = (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);
        } catch (Exception ex) {
            throw new JobExecutionException(ex);
        }
        if (appCtx == null) {
            throw new JobExecutionException("No application context available in scheduler context for key '" + APPLICATION_CONTEXT_KEY
                    + "'");
        }
        return appCtx;
    }

    protected EntityManager getPersistentContext(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext applicationContext = getApplicationContext(jobExecutionContext);
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) applicationContext.getBean("entityManagerFactory");
        return entityManagerFactory.createEntityManager();
    }

    protected void unbindSession(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext applicationContext = getApplicationContext(jobExecutionContext);
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) applicationContext.getBean("entityManagerFactory");
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(entityManagerFactory);
        LOG.debug("Closing JPA EntityManager in OpenEntityManagerInViewInterceptor");
        EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
    }

    protected void bindSession(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext applicationContext = getApplicationContext(jobExecutionContext);
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) applicationContext.getBean("entityManagerFactory");
        EntityManager entityManager = getPersistentContext(jobExecutionContext);
        EntityManagerHolder emHolder = new EntityManagerHolder(entityManager);
        TransactionSynchronizationManager.bindResource(entityManagerFactory, emHolder);
    }

    protected final void reportSuccess(JobExecutionContext context, String specificJobRunInformation) {
        emailJobReport(true, context, specificJobRunInformation, null);
    }

    protected final void reportFailure(JobExecutionContext context, String specificJobRunInformation, Throwable rootCause) {
        emailJobReport(false, context, specificJobRunInformation, rootCause);
    }

    private void emailJobReport(boolean success, JobExecutionContext context, String specificJobRunInformation, Throwable rootCause) {
        if (context == null) {
            throw new IllegalArgumentException("context argument should not be null");
        }
        try {
            ApplicationContext applicationContext = getApplicationContext(context);
            String emailAddress = null;
            try {
                emailAddress = (String) context.getMergedJobDataMap().get(SCHEDULER_REPORT_EMAILADDRESS_DATAMAPKEY);
            } catch (Exception ex) {
                LOG.error("Unexpected exception while retrieving email address from job datamap", ex);
            }
//            EmailService emailService = (EmailService) applicationContext.getBean("asyncMailService");
            String subject;
            if (success) {
                subject = "[SUCCESS]";
            } else {
                subject = "[FAILURE]";
            }
            JobDetail jobDetail = context.getJobDetail();
            if (jobDetail != null) {
                subject = subject.concat(" " + jobDetail.getKey().getName());
            }
            StringBuilder sbTextBody = new StringBuilder("");
            sbTextBody.append("Job details:\n");
            sbTextBody.append("============\n");
            sbTextBody.append(getJobInformationFromContext(context, new Date()));
            if (specificJobRunInformation != null) {
                sbTextBody.append("\n\n");
                sbTextBody.append("Specific job runtime information:\n");
                sbTextBody.append("=================================\n");
                sbTextBody.append(specificJobRunInformation);
            }
            if (rootCause != null) {
                sbTextBody.append("\n\n");
                sbTextBody.append("Root cause of failure:\n");
                sbTextBody.append("======================\n");
                sbTextBody.append(rootCause + "\n");
                for (StackTraceElement stackTraceElement : rootCause.getStackTrace()) {
                    sbTextBody.append("\t" + "at " + stackTraceElement + "\n");
                }
                Throwable underLyingCause = rootCause.getCause();
                while (underLyingCause != null) {
                    sbTextBody.append("\nCaused by: " + underLyingCause + "\n");
                    for (StackTraceElement stackTraceElement : underLyingCause.getStackTrace()) {
                        sbTextBody.append("\t" + "at " + stackTraceElement + "\n");
                    }
                    underLyingCause = underLyingCause.getCause();
                }
            }
            final String bodyText = sbTextBody.toString();
            if (emailAddress == null || "".equals(emailAddress)) {
                LOG.error("Job datamap did not contain a value for the emailaddress: " + SCHEDULER_REPORT_EMAILADDRESS_DATAMAPKEY
                        + ". Not sending report:\n" + bodyText, new RuntimeException("For stack trace purpose"));
                return;
            }
            String bodyHtml = "<html><body><code>\n" + bodyText.replaceAll("\n", "<br />\n").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
                    + "</code></body></html>";
            String fromAddress = "no-reply@example.com";
//            emailService.sendEmail(fromAddress, "Scheduler", emailAddress, subject, bodyText, bodyHtml);
        } catch (Exception ex) {
            LOG.error("Could not mail report", ex);
        }
    }

    private String getJobInformationFromContext(JobExecutionContext context, Date reportTime) {
        StringBuilder sb = new StringBuilder();
        // Job information
        JobDetail jobDetail = context.getJobDetail();
        if (jobDetail != null) {
            sb.append("FullName: " + jobDetail.getKey().getName() + "\n");
            sb.append("JobClass: " + jobDetail.getJobClass().getName() + "\n");
        }
        // Trigger information
        Trigger trigger = context.getTrigger();
        if (trigger != null) {
            sb.append("TriggerName: " + trigger.getKey().getName() + "\n");
        }
        // Time information
        sb.append("ScheduledFireTime: " + context.getScheduledFireTime() + "\n");
        sb.append("FireTime: " + context.getFireTime() + "\n");
        // sb.append("JobRuntTime: " + context.getJobRunTime() + "\n"); returns -1, I think this only is available if
        // Job has returned to the scheduler
        DecimalFormat df = new DecimalFormat("0.##");
        double runTimeInMinutes = (reportTime.getTime() - context.getFireTime().getTime()) / 1000d / 60d;
        sb.append("RunTimeInMinutes: " + df.format(runTimeInMinutes) + "\n");
        sb.append("NextFireTime: " + context.getNextFireTime() + "\n");
        // Data information
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        if (jobDataMap != null && jobDataMap.getKeys().length > 0) {
            sb.append("JobData: \n");
            for (String key : jobDataMap.getKeys()) {
                sb.append("\t" + key + "=" + jobDataMap.get(key) + "\n");
            }
        }
        return sb.toString();
    }

}
