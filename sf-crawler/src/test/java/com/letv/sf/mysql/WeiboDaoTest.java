package com.letv.sf.mysql;

import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.JsoupClient;
import com.letv.sf.http.cookie.support.WeiboCrawler;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/15.
 */
public class WeiboDaoTest {
    public static void main(String[] args) throws IOException {
        CrawlerSlotConfig config = new CrawlerSlotConfig();
        config.setUsername("13810108964");
        config.setPassword("leshi123");
        WeiboCrawler weiboCrawler = new WeiboCrawler(config);
        Map<String, String> cookies = weiboCrawler.login();

        JsoupClient jsoupClient = new JsoupClient(null, cookies);
        String url = "https://api.weibo.com/2/comments/show.json?id=4052548870965401&source=140226478";

        HttpResult result = jsoupClient.get(url);
        System.out.println(result.getContent());
    }
}
