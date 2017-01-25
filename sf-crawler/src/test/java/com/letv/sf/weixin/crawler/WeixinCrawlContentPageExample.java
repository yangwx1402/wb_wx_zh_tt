package com.letv.sf.weixin.crawler;

import com.letv.sf.crawler.Crawl;
import com.letv.sf.crawler.weixin.WeixinPageCrawler;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import com.letv.sf.parser.weixin.WeixinPageParser;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by yangyong3 on 2016/12/6.
 */
public class WeixinCrawlContentPageExample {
    public static void main(String[] args) throws Exception {
        String url = "http://mp.weixin.qq.com/s?src=3&timestamp=1482740315&ver=1&signature=0kSO*XvoUERUAbZNkFmqs1gY7jlPviWMyoQdR3tn-LROxx7wcrmyL*MX0*ls5VdN41D84YJnCNg50qvWEvStRtfgaQ0j4NoIjGBvCXa3s0hbY4-qWFRd*PDAiqakWtbshsNTU1upfN1R3Qe2b*6auyKtwMWd66940Lyihu8gS64=";
        Crawl crawl = new WeixinPageCrawler();
        Parse<YuqingArticle, BeidouEntity> parse = new WeixinPageParser();
        HttpResult<BeidouEntity> httpResult = crawl.crawl(url, null);
        FileUtils.writeStringToFile(new File("E:\\test.html"), httpResult.getContent(), "utf-8");
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("春晚");
        beidou.setTag("春晚");
        httpResult.setMeta(beidou);

        YuqingArticle article = parse.parse(httpResult);
        System.out.println(article.getAttitudes_count() + ":" + article.getReadCount());
        System.out.println(article.getComments().size());

    }
}
