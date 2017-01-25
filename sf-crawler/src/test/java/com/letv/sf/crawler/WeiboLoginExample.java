package com.letv.sf.crawler;

import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.http.cookie.support.WeiboCrawler;
import com.letv.sf.utils.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;


/**
 * Created by yangyong3 on 2017/1/12.
 */
public class WeiboLoginExample {
    public static void main(String[] args) throws Exception {
        //int[] ids = new int[]{172,175,176,179,177,180,181,182,183};
        int[] ids = new int[]{300};
        List<CrawlerSlotConfig> list = DaoFactory.weiboSlotDao.getSlotByType("test",1);
        //for (int id : ids) {
        for(CrawlerSlotConfig config:list){
        //CrawlerSlotConfig config = DaoFactory.weiboSlotDao.getSlotById(id);
            WeiboCrawler weiboCrawler = new WeiboCrawler(config);
            weiboCrawler.login();
            //weiboCrawler.login();
//            String html = weiboCrawler.fetchUrl("http://s.weibo.com/weibo/%25E6%2598%25A5%25E6%2599%259A&Refer=STopic_box");
//            Document document = Jsoup.parse(html);
//            Element yanzhengma = JsoupUtils.selectFirstElement(document, "div.code_ver");
//            if (yanzhengma != null) {
//                System.out.println(yanzhengma.select("input.W_inputStp").html());
//                System.out.println("http://s.weibo.com" + yanzhengma.select("img").attr("src"));
//                System.out.println("需要验证码");
//            }
        }
    }
}
