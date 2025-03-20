package com.myprojects.reminder.quartzJob;

import com.myprojects.reminder.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
//@DisallowConcurrentExecution for concurrency
@RequiredArgsConstructor
@Slf4j
public class EmailJob extends QuartzJobBean {

    private final EmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        String recipient = jobDataMap.getString("recipient");
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");

        log.info("Sending email to: {}", recipient);
        emailService.sendEmail(recipient, subject, body);
    }
}
