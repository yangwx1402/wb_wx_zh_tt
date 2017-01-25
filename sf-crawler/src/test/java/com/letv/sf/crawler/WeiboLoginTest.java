package com.letv.sf.crawler;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.support.WeiboCrawler;
import com.letv.sf.utils.JsoupUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

/**
 * Created by yangyong3 on 2017/1/12.
 */
public class WeiboLoginTest {
    public static void main(String[] args) throws Exception {
        for (int i = 301; i <= 301; i++) {
            CrawlerSlotConfig config = DaoFactory.weiboSlotDao.getSlotById(i);
            WeiboCrawler weiboCrawler = new WeiboCrawler(config);
            String html = weiboCrawler.fetchUrl("http://s.weibo.com/weibo/央视春晚&Refer=STopic_box");
            FileUtils.writeStringToFile(new File("E:\\weibo", "id_" + i + ".html"), html, "utf-8", false);
            Document document = Jsoup.parse(html);
            Element yanzhengma = JsoupUtils.selectFirstElement(document, "div.code_ver");
            if (yanzhengma != null) {
                //这里可以update一下slot的状态为验证码
                DaoFactory.weiboSlotDao.updateIsCode(config.getId(), 1);
            }
        }
    }
}
