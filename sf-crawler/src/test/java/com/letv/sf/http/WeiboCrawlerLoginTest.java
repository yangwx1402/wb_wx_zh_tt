package com.letv.sf.http;

import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.support.WeiboCrawler;

import java.io.IOException;

/**
 * Created by yangyong3 on 2016/12/15.
 */
public class WeiboCrawlerLoginTest {
    public static void main(String[] args) throws IOException {
        CrawlerSlotConfig config = new CrawlerSlotConfig();
        config.setUsername("orteryini@msn.com");
        config.setPassword("asd1681");
        WeiboCrawler weiboCrawler = new WeiboCrawler(config);
        System.out.println(weiboCrawler.login());
    }
}
