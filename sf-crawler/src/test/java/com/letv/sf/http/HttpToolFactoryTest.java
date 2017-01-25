package com.letv.sf.http;

import com.letv.sf.config.CrawlerSlotType;

/**
 * Created by yangyong3 on 2016/12/14.
 */
public class HttpToolFactoryTest {
    public static void main(String[] args) {
       System.out.println(HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.weibo.toString()).getJsoupClient().getProxy().getIp());
    }
}
