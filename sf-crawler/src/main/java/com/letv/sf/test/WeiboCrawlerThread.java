package com.letv.sf.test;

import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by yangyong3 on 2017/1/10.
 */
public class WeiboCrawlerThread implements Runnable {

    private String keyword;

    private String dist;

    public WeiboCrawlerThread( String keyword,String dist) {
        this.keyword = keyword;
        this.dist = dist;
    }


    public void run() {
        String url = "http://s.weibo.com/weibo/" + keyword + "?topnav=1&wvr=6&b=1";

        int count = 0;
        try {
            while (count++ < 1000) {
                HttpResult html = HttpToolFactory.getHttpToolGroupByType("weibo").getUrlByHtmlUnit(url);
                FileUtils.writeStringToFile(new File(dist, keyword + "_" + System.currentTimeMillis()), html.getContent(), "utf-8", false);
                Thread.sleep(30000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}