package pe.gob.senamhi.util;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import pe.gob.senamhi.enums.QuartzJobEnum;

public class QuartzUtil {

	private static JobDetail job = null;
	private static Trigger trigger = null;
	
	public static void detenerTrigger(Scheduler scheduler, QuartzJobEnum quartzJobEnum) throws Exception{
		job = newJob(quartzJobEnum.job()).withIdentity(quartzJobEnum.getQuartzBean().getJobId()).build();
		scheduler.interrupt(job.getKey());
		scheduler.deleteJob(job.getKey());
		scheduler.unscheduleJob(new TriggerKey(quartzJobEnum.getQuartzBean().getTriggerId()));
	}
	
	public static void registrarTrigger(Scheduler scheduler, QuartzJobEnum quartzJobEnum) throws Exception{
		System.out.println("quartzJobEnum.getQuartzBean().getJobId() " + quartzJobEnum.getQuartzBean().getJobId());
		System.out.println("quartzJobEnum.getQuartzBean().getFrecuencia() " + quartzJobEnum.getQuartzBean().getFrecuencia());
		System.out.println("quartzJobEnum.getQuartzBean().getTriggerId() " + quartzJobEnum.getQuartzBean().getTriggerId());
		// Setup the Job class
        job = newJob(quartzJobEnum.job()).withIdentity(quartzJobEnum.getQuartzBean().getJobId()).build();

        // Create a Trigger
        trigger = newTrigger()
        .withIdentity(quartzJobEnum.getQuartzBean().getTriggerId())
        .withSchedule(CronScheduleBuilder.cronSchedule(quartzJobEnum.getQuartzBean().getFrecuencia()))
        .build();

        // Setup the Job and Trigger with Scheduler & schedule jobs
        scheduler.scheduleJob(job, trigger);
        
	}
	
	
}
