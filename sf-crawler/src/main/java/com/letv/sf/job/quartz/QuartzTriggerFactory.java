package com.letv.sf.job.quartz;

import org.quartz.*;

import java.text.ParseException;

/**
 * Created by yangyong3 on 2016/12/4.
 */
public class QuartzTriggerFactory {

    public static Trigger getCronTrigger(String triggerGroup, String triggerName, String cron) throws ParseException {
        return TriggerBuilder.
                newTrigger().
                withIdentity(triggerName, triggerGroup).
                withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
    }
}
