package com.letv.sf.toutiao.crawler;

import com.letv.sf.job.thread.toutiao.ToutiaoKeywordSearchThread;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class MutilThreadToutiaoSearch {
    public static void main(String[] args) {
        Thread thread = new Thread(new ToutiaoKeywordSearchThread());
        thread.start();
    }
}
