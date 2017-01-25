package com.letv.sf.crawler;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.JsoupClient;
import com.letv.sf.http.cookie.support.WeiboCrawler;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/15.
 */
public class JsoupClientCookieTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        CrawlerSlotConfig  config = DaoFactory.weiboSlotDao.getSlotById(337);

        WeiboCrawler weiboCrawler = new WeiboCrawler(config);
        Map<String,String> cookies = weiboCrawler.login();
        for(Map.Entry<String,String> entry:cookies.entrySet()){
            System.out.println(entry.getKey()+"="+ entry.getValue());

        }
        JsoupClient jsoupClient = new JsoupClient(config,cookies);
        Thread.sleep(6000);
        weiboCrawler.login();
        String url = "https://api.weibo.com/2/comments/show.json?id=4052548870965401&source=140226478";

        HttpResult result = jsoupClient.get(url);
        System.out.println(result.getContent());
    }
}
