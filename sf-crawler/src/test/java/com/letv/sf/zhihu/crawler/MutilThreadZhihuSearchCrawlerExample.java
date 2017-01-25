package com.letv.sf.zhihu.crawler;

import com.letv.sf.job.beidou.MoiveSearchWordPubJob;
import com.letv.sf.job.thread.zhihu.ZhihuSearchCrawlerThread;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class MutilThreadZhihuSearchCrawlerExample {
    public static void main(String[] args) throws JobExecutionException, InterruptedException {
        MoiveSearchWordPubJob job = new MoiveSearchWordPubJob();
        job.execute(null);
        Thread.sleep(1000);

        Thread thread = new Thread(new ZhihuSearchCrawlerThread());
        thread.start();
    }
}
