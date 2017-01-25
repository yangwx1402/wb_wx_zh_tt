package com.letv.sf.crawler;

import com.letv.sf.job.thread.weibo.KeywordSearchHistoryThread;
import com.letv.sf.job.thread.weibo.KeywordSearchThread;
import com.letv.sf.job.thread.weibo.WeiboSearchHistoryCrawlerThread;
import org.quartz.JobExecutionException;

/**
 * Created by yangyong3 on 2016/12/1.
 */
public class MutliThreadKeyWordHistorySearchExample {

    public static void main(String[] args) throws JobExecutionException, InterruptedException {
        KeywordSearchHistoryThread pubThread = new KeywordSearchHistoryThread();
        new Thread(pubThread).start();
    }

}
