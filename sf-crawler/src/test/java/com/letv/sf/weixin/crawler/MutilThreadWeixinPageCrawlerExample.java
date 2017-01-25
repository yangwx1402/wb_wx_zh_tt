package com.letv.sf.weixin.crawler;

import com.letv.sf.job.thread.weixin.WeixinPageCrawlerThread;
import com.letv.sf.job.weixin.WeixinCommentSeedPubJob;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public class MutilThreadWeixinPageCrawlerExample {
    public static void main(String[] args) throws JobExecutionException {
        Thread thread = new Thread(new WeixinPageCrawlerThread());
        thread.start();
    }
}
