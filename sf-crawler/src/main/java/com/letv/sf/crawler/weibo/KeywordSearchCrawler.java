package com.letv.sf.crawler.weibo;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.crawler.AbstractCrawler;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.entity.common.SpiderStatus;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.utils.JsoupUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/30.
 * 微博关键词搜索爬虫
 */
public class KeywordSearchCrawler extends AbstractCrawler {

    private static final int NEED_CODE = 1;

    private static final int NEED_PHONE = 2;

    private static final Logger log = Logger.getLogger(KeywordSearchCrawler.class);

    /**
     * 微博关键词搜索爬取
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public HttpResult crawl(String url, Map<String, String> params) throws Exception {
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.weibo.toString());
        HttpResult result = null;
        log.info("crawl url by htmlunit url -[" + url + "]");
        result = group.getUrlByHtmlUnit(url);
        if (result != null) {
            result.setUrl(url);
            Document document = Jsoup.parse(result.getContent());
            Element shouji = JsoupUtils.selectFirstElement(document,"p.page.W_f14");
            if(shouji!=null){
                String shoujiyanzheng = shouji.text();
                if(shoujiyanzheng.contains("短信验证")){
                    DaoFactory.weiboSlotDao.updateIsCode(group.getId(),NEED_PHONE);
                }
            }
            Element yanzhengma = JsoupUtils.selectFirstElement(document, "div.code_ver");
            if (yanzhengma != null) {
               //这里可以update一下slot的状态为验证码
                DaoFactory.weiboSlotDao.updateIsCode(group.getId(),NEED_CODE);
            }

        }
        return result;
    }
}
