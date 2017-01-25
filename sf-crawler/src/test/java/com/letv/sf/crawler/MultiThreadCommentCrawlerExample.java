package com.letv.sf.crawler;

import com.letv.sf.job.thread.weibo.CommentCralwerThread;
import com.letv.sf.job.weibo.WeiboCommentSeedPubJob;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/2.
 */
public class MultiThreadCommentCrawlerExample {
    public static void main(String[] args) throws JobExecutionException {
        for (int i = 0; i < 1; i++) {
            Thread thread = new Thread(new CommentCralwerThread());
            thread.start();
        }
    }
}
