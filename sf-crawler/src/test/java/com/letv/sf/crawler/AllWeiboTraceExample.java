package com.letv.sf.crawler;

import com.letv.sf.job.weibo.WeiboTraceCrawlerJob;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/2.
 */
public class AllWeiboTraceExample {
    public static void main(String[] args) throws JobExecutionException {
        WeiboTraceCrawlerJob job = new WeiboTraceCrawlerJob();
        job.execute(null);
    }
}
