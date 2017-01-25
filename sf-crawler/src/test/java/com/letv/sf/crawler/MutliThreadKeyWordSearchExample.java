package com.letv.sf.crawler;

import com.letv.sf.job.thread.weibo.KeywordSearchThread;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class MutliThreadKeyWordSearchExample {

    public static void main(String[] args) throws JobExecutionException, InterruptedException {
        KeywordSearchThread pubThread = new KeywordSearchThread();
        new Thread(pubThread).start();
    }

}
