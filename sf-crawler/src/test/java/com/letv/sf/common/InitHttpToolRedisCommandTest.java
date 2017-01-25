package com.letv.sf.common;

import com.letv.sf.job.common.SedHttpToolRedisCommanderJob;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2017/1/20.
 */
public class InitHttpToolRedisCommandTest {

    public static void main(String[] args) throws JobExecutionException {
        SedHttpToolRedisCommanderJob job = new SedHttpToolRedisCommanderJob();
        job.execute(null);
    }
}
