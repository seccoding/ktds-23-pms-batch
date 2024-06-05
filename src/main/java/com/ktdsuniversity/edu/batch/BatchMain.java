package com.ktdsuniversity.edu.batch;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktdsuniversity.edu.batch.jobs.LogoutJob;
import com.ktdsuniversity.edu.batch.jobs.TestJob;

public class BatchMain {

	private static final Logger logger = LoggerFactory
			.getLogger(BatchMain.class);

	public static void main(String[] args) {
		Scheduler scheduler = makeScheduler();

		if (scheduler != null) {
			addSchedule(scheduler, LogoutJob.class);
//			addSchedule(scheduler, TestJob.class);
//			addSchedule(scheduler, TestJob2.class);

			start(scheduler);
		}
	}

	private static Scheduler makeScheduler() {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			return schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	private static void start(Scheduler scheduler) {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
	}

	private static void addSchedule(Scheduler scheduler,
			Class<? extends Job> jobDetail) {

		String jobName = getFinalField(jobDetail, "JOB_NAME");
		JobDetail job = createJobDetail(jobDetail, jobName);

		String cronSchedule = getFinalField(jobDetail, "SCHEDULE");
		CronTrigger jobTrigger = createJobTrigger(job, jobName, cronSchedule);

		Set<Trigger> jobTriggerSet = new HashSet<>();
		jobTriggerSet.add(jobTrigger);

		try {
			scheduler.scheduleJob(job, jobTriggerSet, false);
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
	}

	private static String getFinalField(Class<? extends Job> jobDetail,
			String staticFieldName) {
		try {
			Field schedule = jobDetail.getField(staticFieldName);
			if (schedule != null) {
				schedule.setAccessible(true);
				return schedule.get(jobDetail).toString();
			}
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(
					jobDetail + ": " + staticFieldName + " 상수가 필요합니다. ");
		}

		return null;
	}

	private static JobDetail createJobDetail(Class<? extends Job> jobDetail,
			String jobName) {
		JobDetail job = JobBuilder.newJob(jobDetail)
				.withIdentity(jobName, "job_group").build();
		return job;
	}

	private static CronTrigger createJobTrigger(JobDetail jobDetail,
			String jobName, String cronSchedule) {
		return TriggerBuilder.newTrigger()
				.withIdentity(jobName, jobName + "cron_trigger_group")
				.withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
				.forJob(jobDetail).build();
	}

}
