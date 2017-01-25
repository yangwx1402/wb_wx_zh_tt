package com.letv.sf.bootstrap.hook;

import com.letv.sf.counter.CounterTool;
import com.letv.sf.dao.DataSourceFactory;
import com.letv.sf.dao.MongoDataSourceFactory;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.job.quartz.QuartzScheduler;
import com.letv.sf.job.thread.CrawlerThreadPool;
import com.letv.sf.mq.MessageFactory;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import java.util.Date;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public class ExitHookThread extends Thread {

    private static final Logger log = Logger.getLogger(ExitHookThread.class);

    private static final long one_minute = 1000 * 60;

    private static final long one_hour = one_minute * 60;

    private CrawlerThreadPool threadPool;

    private long start;

    public ExitHookThread(CrawlerThreadPool threadPool, long start) {
        this.start = start;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        if (threadPool != null)
            threadPool.shutdown(true);
        try {
            QuartzScheduler.shutdownSchedule();
            log.info("QuartzScheduler shutdown finished");
        } catch (SchedulerException e) {
            log.error("QuartzScheduler shutdown error", e);
        }
        DataSourceFactory.destory();
        log.info("mysql dataSource destory finished ");
        MongoDataSourceFactory.destory();
        log.info("mongo datasource destory finished ");
        HttpToolFactory.destory();
        log.info("httpToolFactory destory finished ");
        MessageFactory.destory();
        log.info("MessageQueue destory finished ");
        long duration = System.currentTimeMillis() - start;
        if (duration < one_minute) {
            log.info("log spider is stopped, now time is " + new Date() + ", spider run duration " + (duration / one_minute) + " minutes");
            log.info("spider counter info is "+ CounterTool.print());
        } else {
            log.info("log spider is stopped, now time is " + new Date() + ", spider run duration " + (duration / one_hour) + " hours");
            log.info("spider counter info is "+ CounterTool.print());
        }

    }
}
