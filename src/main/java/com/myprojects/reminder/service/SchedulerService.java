package com.myprojects.reminder.service;

import com.myprojects.reminder.quartzJob.EmailJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class SchedulerService {
    private final Scheduler scheduler;

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleEmail(String recipient, String subject, String body, long delay) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(recipient, subject, body);
        Trigger trigger = buildTrigger(jobDetail,delay);

        log.info("Scheduling email to be sent to {}", recipient);
        scheduler.scheduleJob(jobDetail,trigger);
    }

    private JobDetail buildJobDetail(String recipient, String subject, String body) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("recipient", recipient);
        jobDataMap.put("subject", subject);
        jobDataMap.put("body", body);

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, long delay) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(),"email-triggers")
                .startAt(new Date(System.currentTimeMillis() + delay * 60000))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
