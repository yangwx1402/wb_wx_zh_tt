package com.letv.sf.test;

import com.letv.sf.job.thread.CrawlerThreadPool;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/10.
 */
public class WeiboCrawlerRessureTest {


    public static void main(String[] args) throws IOException {
        CrawlerThreadPool pool = new CrawlerThreadPool();
        String[] keywords = new String[]{"春晚节目单", "春晚导演", "春晚主持人","tfboys鹿晗","央视春晚","春晚 VR"};
        for (int i=0;i<Integer.parseInt(args[1]);i++) {
            pool.execute(new WeiboCrawlerThread(keywords[i],args[0]));
        }
    }
}
