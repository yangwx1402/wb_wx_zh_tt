package com.letv.sf.crawler;

import com.letv.sf.job.beidou.MoiveSearchWordPubJob;
import com.letv.sf.job.weibo.WeiboTopicCrawlerJob;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/2.
 */
public class WeiboTopicCrawlerExample {
    public static void main(String[] args) throws JobExecutionException, InterruptedException {
        while(true) {
            WeiboTopicCrawlerJob job = new WeiboTopicCrawlerJob();
            job.execute(null);
            Thread.sleep(1000*300);
        }
    }
}
