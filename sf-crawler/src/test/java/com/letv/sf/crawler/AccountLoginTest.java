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
import java.util.List;

/**
 * Created by yangyong3 on 2017/1/19.
 */
public class AccountLoginTest {
    public static void main(String[] args) throws Exception {
        List<CrawlerSlotConfig> configs = DaoFactory.weiboSlotDao.getSlotByType("weibo", 0);
        File file = new File("E:\\weibo_ids.txt");
        for (CrawlerSlotConfig config : configs) {
            WeiboCrawler weiboCrawler = new WeiboCrawler(config);
            String html = null;
            try {
                html = weiboCrawler.fetchUrl("http://s.weibo.com/weibo/央视春晚&Refer=STopic_box");
                if (!html.contains("CONFIG['islogin'] = '1'")) {
                    FileUtils.writeStringToFile(file, config.getId() + "\n", "utf-8", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                FileUtils.writeStringToFile(file, config.getId() + "\n", "utf-8", true);
            }
        }
    }
}
