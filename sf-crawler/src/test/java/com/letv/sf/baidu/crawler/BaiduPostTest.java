package com.letv.sf.baidu.crawler;

import com.letv.sf.config.CrawlerSlotType;
import com.letv.sf.http.HttpResult;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by yangyong3 on 2016/12/13.
 */
public class BaiduPostTest {
    public static void main(String[] args) throws IOException {
        String url = "http://tieba.baidu.com/f?ie=utf-8&kw=%E7%9B%B4%E9%80%9A%E6%98%A5%E6%99%9A&fr=search";
        HttpResult temp = HttpToolFactory.getHttpToolGroupByType(CrawlerSlotType.baidu.toString()).getJsoupClient().get(url);
        Document document = Jsoup.parse(temp.getContent());
        Elements elements = document.select("a.card_title_fname");
        System.out.println(elements.get(0).text());
        Element element = JsoupUtils.selectFirstElement(document,"span.card_menNum");
        System.out.println(element.text());
    }
}
