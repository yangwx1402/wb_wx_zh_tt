package com.letv.sf.cookie;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.letv.sf.dao.slot.CrawlerSlotDao;
import com.letv.sf.dao.slot.CrawlerSlotDaoByMysql;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.support.WeiboCrawler;

import java.util.Map;
import java.util.Set;

/**
 * Created by yangyong3 on 2017/1/11.
 */
public class CookieFactory {
    public static void main(String[] args) {
        CrawlerSlotDao dao = new CrawlerSlotDaoByMysql();
        String username = "janm61068@163.com";
        String password = "0lyojoe";
        String type = "weibo";
        System.out.println("分割线");
        CrawlerSlotConfig slotConfig = new CrawlerSlotConfig();
        slotConfig.setUsername(username);
        slotConfig.setPassword(password);
        slotConfig.setType(type);
        WeiboCrawler weiboCrawler = new WeiboCrawler();
//        Set<Cookie> cookie = weiboCrawler.getCookeFromPersist(slotConfig.getUsername(), slotConfig.getPassword(), slotConfig.getType());
//
//        for (Cookie cook : cookie) {
//            System.out.println(cook);
//        }
//        System.out.println(cookie.size());

    }
}
