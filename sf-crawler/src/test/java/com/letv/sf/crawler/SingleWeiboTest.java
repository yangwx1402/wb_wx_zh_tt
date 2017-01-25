package com.letv.sf.crawler;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.support.WeiboCrawler;
import com.letv.sf.utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by yangyong3 on 2017/1/17.
 */
public class SingleWeiboTest {
    public static void main(String[] args) throws Exception {
        CrawlerSlotConfig config = DaoFactory.weiboSlotDao.getSlotById(2);
        WeiboCrawler weiboCrawler = new WeiboCrawler(config);
        String html = weiboCrawler.fetchUrl("http://s.weibo.com/weibo/央视春晚&Refer=STopic_box");
        System.out.println(html);
        Document document = Jsoup.parse(html);
        Element yanzhengma = JsoupUtils.selectFirstElement(document, "div.code_ver");
        if (yanzhengma != null) {
            //这里可以update一下slot的状态为验证码
            DaoFactory.weiboSlotDao.updateIsCode(config.getId(), 1);
        }
    }
}
