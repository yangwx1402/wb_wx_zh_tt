package com.letv.sf.job.quartz;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Map;

/**
 * quartz 调度器
 * @author yangyong3
 *
 */
public class QuartzScheduler {

	private static Scheduler sechduler;

	public static boolean quartz_schedule_start = false;
	
	private static final Logger loger = Logger.getLogger(QuartzScheduler.class);
    //生成调度器
	private synchronized static Scheduler getScheduler()
			throws SchedulerException {
		if (sechduler == null || sechduler.isShutdown())
			sechduler = StdSchedulerFactory.getDefaultScheduler();
		return sechduler;
	}
    //启动调度器
	public synchronized static void startSchedule() throws SchedulerException {
		getScheduler().start();
		quartz_schedule_start = true;
	}
    //关闭调度器
	public synchronized static void shutdownSchedule()
			throws SchedulerException {
		if (sechduler != null) {
			sechduler.shutdown(false);
			sechduler = null;
		}
		quartz_schedule_start = false;
	}
    //添加job到调度器中
	public synchronized static boolean addJob(JobDetail job, Trigger trigger)
			throws SchedulerException {
		JobDetail tempJob = null;
		if (sechduler == null || sechduler.isShutdown()) {
			loger.error("sechduler is not started or shutdown --please start sechduler first---");
			return false;
		}
		tempJob = sechduler.getJobDetail(job.getKey());
		if(tempJob!=null)
			sechduler.deleteJob(job.getKey());
		sechduler.scheduleJob(job, trigger);
		loger.info("--start quartz job ,job ="+job.getKey().getGroup()+"."+job.getKey().getName());
		return true;
	}

	public static JobDetail getJobDetail(String job_group,String job_name,Class clazz,Map<String,Object> params){
		JobDetail jobDetail = JobBuilder.newJob().withIdentity(job_name,job_group).ofType(clazz).build();
		if(params!=null&&params.size()>0)
			jobDetail.getJobDataMap().putAll(params);
		return jobDetail;
	}
	

	public synchronized static boolean delJob(String job_group,String job_name) throws SchedulerException{
		if (sechduler == null || sechduler.isShutdown()) {
			loger.error("sechduler is not start or is shutdown --please start sechduler first---");
			return false;
		}
		return sechduler.deleteJob(JobKey.jobKey(job_name,job_group));
	}
	
	public synchronized static boolean resetJob(JobDetail job,String trigger_name,String trigger_group) throws SchedulerException{
		if (sechduler == null || sechduler.isShutdown()) {
			loger.error("sechduler is not start or is shutdown --please start sechduler first---");
			return false;
		}
		Trigger trigger = sechduler.getTrigger(TriggerKey.triggerKey(trigger_name, trigger_group));
		sechduler.scheduleJob(job, trigger);
		return true;
	}
}
