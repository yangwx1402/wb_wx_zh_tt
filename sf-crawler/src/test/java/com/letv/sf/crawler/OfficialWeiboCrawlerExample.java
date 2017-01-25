package com.letv.sf.crawler;


import com.letv.sf.job.thread.weibo.OfficialWeiboCrawlerThread;
import com.letv.sf.job.weibo.OfficialWeiboSeedPushJob;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class OfficialWeiboCrawlerExample {
    public static void main(String[] args) throws Exception {
        OfficialWeiboSeedPushJob job = new OfficialWeiboSeedPushJob();
       // job.execute(null);
        new Thread(new OfficialWeiboCrawlerThread()).start();
    }
}
