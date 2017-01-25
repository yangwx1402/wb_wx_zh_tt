package com.letv.sf.crawler;

import com.letv.sf.crawler.weibo.WeiboUserCrawler;
import com.letv.sf.dao.weibo.WeiboDao;
import com.letv.sf.dao.weibo.WeiboDaoByMysql;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.weibo.WeiboUserParser;
import weibo4j.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/29.
 */
public class WeiboUserCrawlerExample {
    public static void main(String[] args) throws Exception {
        WeiboUserCrawler crawler = new WeiboUserCrawler();
        String url = "https://api.weibo.com/2/users/show.json?source=140226478&uid=3975192345";
        Map<String,String> map = new HashMap<String,String>();
        HttpResult result = crawler.crawl(url,map);
        System.out.println(result.getContent());
        WeiboUserParser parser = new WeiboUserParser();
        User user = parser.parse(result);
        System.out.println(user.getId());
        WeiboDao dao = new WeiboDaoByMysql();
        dao.saveWeiboUser(user);
    }

}
