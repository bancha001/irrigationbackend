package com.duke.irrigator.business;

import com.duke.irrigator.infrastructure.ValveJob;
import com.duke.irrigator.model.ScheduleDetail;
import com.duke.irrigator.model.ScheduleRequest;
import com.duke.irrigator.model.ScheduleResponse;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

@Component
public class DefaultScheduleBusiness implements ScheduleBusiness {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleBusiness.class);

	@Autowired
	private Scheduler scheduler;

	@Override
	public ScheduleResponse createSchedule(ScheduleRequest scheduleRequest) {
		ScheduleResponse scheduleResponse = null;
		try {
			logger.info("createSchedule is invoked");
			JobDetail jobDetail = buildJobDetail(scheduleRequest);
			ZonedDateTime dateTime = ZonedDateTime.of(scheduleRequest.getDateTime(), scheduleRequest.getTimeZone());
			Trigger trigger = buildJobTrigger(jobDetail,dateTime);
			scheduler.scheduleJob(jobDetail, trigger);

			scheduleResponse = new ScheduleResponse(true, "Schedule is created Successfully!");
			logger.info("createSchedule is done");

		} catch (SchedulerException ex) {
			logger.error("Error creating a schedule", ex);
			scheduleResponse = new ScheduleResponse(false, "Error creating schedule: " + ex.toString());
		}

		return scheduleResponse;
	}

	@Override
	public List<ScheduleDetail> listSchedule() {
		List<ScheduleDetail> result = new ArrayList<ScheduleDetail>();
		try {
			logger.info("listSchedule is invoked");
			for (String group : scheduler.getJobGroupNames()) {

				for (JobKey jobKey : scheduler.getJobKeys(groupEquals(group))) {
					System.out.println("Group: " + group);
					System.out.println("Found job identified by: " + jobKey);
					List triggerList = scheduler.getTriggersOfJob(jobKey);
					for (Object trigger : triggerList) {
						ScheduleDetail scheduleDetail = new ScheduleDetail();
						scheduleDetail.setNextFireTime(((Trigger) trigger).getPreviousFireTime());
						scheduleDetail.setPreviousFireTime(((Trigger) trigger).getPreviousFireTime());
						scheduleDetail.setGroupName(jobKey.getGroup());
						scheduleDetail.setJobName((jobKey.getName()));
						result.add(scheduleDetail);
					}
				}
			}
			logger.info("listSchedule is done");

		} catch (SchedulerException ex) {
			logger.error("Error listing schedule", ex);
		}

		return result;
	}

	@Override
	public ScheduleResponse deleteSchedule(ScheduleDetail scheduleDetail) {
		ScheduleResponse scheduleResponse = null;
		try {
			logger.info("deleteSchedule is invoked");
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(scheduleDetail.getGroupName()))) {
				if (scheduleDetail.getJobName().equalsIgnoreCase(jobKey.getName())) {
					scheduler.deleteJob(jobKey);
					scheduleResponse = new ScheduleResponse(true, "Successfully delete schedule");
				}
			}
			logger.info("deleteSchedule is done");
		}catch(Exception e){
			logger.error("Error deleting schedule", e);
			scheduleResponse = new ScheduleResponse(false, "Error creating schedule: " + e.toString());
		}

		return scheduleResponse;
	}

	private JobDetail buildJobDetail(ScheduleRequest scheduleRequest) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("")

		return JobBuilder.newJob(ValveJob.class)
				.withIdentity(UUID.randomUUID().toString(), "irrigation-jobs")
				.withDescription("Irrigation Job")
				.usingJobData(jobDataMap)
				.storeDurably()
				.build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "irrigation-triggers")
				.withDescription("Irrigation Trigger")
				.startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInHours(24)
						.repeatForever())
				.build();
	}

}
