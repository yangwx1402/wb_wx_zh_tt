package com.letv.sf.crawler;

import com.letv.sf.crawler.Thread.WeiboCrawlerThread;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.job.thread.CrawlerThreadPool;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/10.
 */
public class WeiboCrawlerRessureTest {


    public static void main(String[] args) throws IOException {
        CrawlerThreadPool pool = new CrawlerThreadPool();
        String[] keywords = new String[]{"春晚节目单", "春晚导演", "春晚主持人"};
        for (String key : keywords) {
            pool.execute(new WeiboCrawlerThread(key));
        }
    }
}
