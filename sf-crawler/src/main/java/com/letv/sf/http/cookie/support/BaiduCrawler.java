package com.letv.sf.http.cookie.support;

import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.CookieCrawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/15.
 */
public class BaiduCrawler extends AbstractCookieCrawler implements CookieCrawler {

    public BaiduCrawler() {
    }


    public BaiduCrawler(CrawlerSlotConfig slotConfig) {
        super(slotConfig);
    }

    @Override
    protected Map<String, String> analogLogin() throws IOException {
        return new HashMap<String,String>();
    }
}
