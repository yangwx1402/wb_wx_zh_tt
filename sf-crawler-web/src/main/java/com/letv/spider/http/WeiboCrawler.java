package com.letv.spider.http;

import com.letv.spider.entity.CrawlerSlotConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/29.
 * 通过自动执行网页登录微博获取cookie信息
 */
public class WeiboCrawler extends AbstractCookieCrawler {

    private static final Logger log = Logger.getLogger(WeiboCrawler.class);

    public WeiboCrawler(CrawlerSlotConfig slotConfig) {
        super(slotConfig);
    }

    /**
     * 实现自动登录微博
     *
     * @return cookie
     * @throws IOException
     */
    protected Map<String, String> analogLogin() throws IOException {
        return new HashMap<String, String>();
    }
}
