package com.letv.sf.weixin.crawler;

import com.letv.sf.job.thread.weixin.WeixinSearchCrawlerThread;

/**
 * Created by yangyong3 on 2016/12/26.
 */
public class MutilThreadWeixinSearchExample {
    public static void main(String[] args){
      Thread thread = new Thread(new WeixinSearchCrawlerThread());
        thread.start();
    }
}
