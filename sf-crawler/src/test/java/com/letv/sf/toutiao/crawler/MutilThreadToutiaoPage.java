package com.letv.sf.toutiao.crawler;

import com.letv.sf.job.thread.toutiao.ToutiaoPageCommentThread;

/**
 * Created by yangyong3 on 2017/1/6.
 */
public class MutilThreadToutiaoPage {
    public static void main(String[] args) {
        Thread thread = new Thread(new ToutiaoPageCommentThread());
        thread.start();
    }
}
