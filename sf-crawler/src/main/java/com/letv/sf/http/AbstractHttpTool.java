package com.letv.sf.http;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.cookie.CookieCrawler;
import com.letv.sf.http.cookie.support.*;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/11.
 */
public abstract class AbstractHttpTool {

    /**
     * 初始化一个爬取器槽位
     *
     * @param config
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected HttpToolGroup initSlot(CrawlerSlotConfig config) throws IOException, ClassNotFoundException {
        HttpToolGroup group = new HttpToolGroup();
        CookieCrawler cookieCrawler = null;
        JsoupClient jsoupClient = null;
        if (CrawlerSlotType.weibo.toString().equals(config.getType())) {
            cookieCrawler = new WeiboCrawler(config);
            jsoupClient = new JsoupClient(config);
        } else if (CrawlerSlotType.weixin.toString().equals(config.getType())) {
            cookieCrawler = new WeixinCrawler(config);
            jsoupClient = new JsoupClient(config);
        } else if (CrawlerSlotType.zhihu.toString().equals(config.getType())) {
            cookieCrawler = new ZhihuCrawler(config);
            jsoupClient = new JsoupClient(config);
        } else if (CrawlerSlotType.baidu.toString().equals(config.getType())) {
            cookieCrawler = new BaiduCrawler(config);
            jsoupClient = new JsoupClient(config, null, null);
        } else if (CrawlerSlotType.common.toString().equals(config.getType())) {
            cookieCrawler = new CommonCookieCrawler(config);
            jsoupClient = new JsoupClient(config);
        } else if (CrawlerSlotType.test.toString().equals(config.getType())) {
            cookieCrawler = new WeiboCrawler(config);
            jsoupClient = new JsoupClient(config);
        }
        group.setCookieCrawler(cookieCrawler);
        group.setApp_key(config.getWeibo_key());
        group.setJsoupClient(jsoupClient);
        group.setType(config.getType());
        group.setId(config.getId());
        return group;

    }
}
