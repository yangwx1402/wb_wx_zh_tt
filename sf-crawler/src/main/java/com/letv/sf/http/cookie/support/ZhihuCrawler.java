package com.letv.sf.http.cookie.support;

import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.CookieCrawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/15.
 */
public class ZhihuCrawler extends AbstractCookieCrawler implements CookieCrawler {

    public ZhihuCrawler() {
    }

    public ZhihuCrawler(CrawlerSlotConfig slotConfig) {
        super(slotConfig);
    }

    /**
     * 知乎暂时没有进行自动登录,原因是需要处理验证码,所以只是从数据库中拿手工配置好的cookie
     *
     * @return
     * @throws IOException
     */
    @Override
    protected Map<String, String> analogLogin() throws IOException {
        return new HashMap<String,String>();
    }

}
