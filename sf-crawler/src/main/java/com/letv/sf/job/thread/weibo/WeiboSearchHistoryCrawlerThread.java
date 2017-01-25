package com.letv.sf.job.thread.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.ContentType;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import weibo4j.model.Status;

import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/22.
 */
public class WeiboSearchHistoryCrawlerThread extends Thread {
    private static final int default_seed = 10;
    @Override
    public void run() {
        BeidouEntity beidou = null;
        for (int i = 1; i < 6; i++) {
            String start = DateUtils.dateString(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * (i + 1)), "yyyy-MM-dd");
            String end = DateUtils.dateString(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * (i)), "yyyy-MM-dd");
            searchByKeyword(start, end);
        }
    }

    private BeidouEntity getBeidouEntity() {
        BeidouEntity beidou = new BeidouEntity();
        beidou.setBeidou_id(291701001l);
        beidou.setEvent_name("2017央视春晚");
        beidou.setTag("春晚");
        return beidou;
    }

    private void searchByKeyword(String start, String end) {
        BeidouEntity keyword = getBeidouEntity();
        CrawlerResultEntity<Status> weiboResultEntity = null;
        String url = "http://s.weibo.com/weibo/" + keyword.getTag() + "&typeall=1&suball=1&timescope=custom:" + start + ":" + end + "&Refer=g";
        int page = 1;
        HttpResult result = null;
        weiboResultEntity = null;
        List<Status> list = null;
        while (true) {
            //抓取到最大页数,不行就闪
            if (page == SpiderConfig.getSinaWeiboSearchMaxPage()) break;
            try {
                result = CrawlerFactory.keyWordCrawler.crawl(url + "&page=" + page, null);
                if (result == null)
                    break;
                weiboResultEntity = ParseFactory.keywordParser.parse(result);
//                if (SpiderStatus.NO_RESULT == weiboResultEntity.getStatus()) {
//                    System.out.println("没有内容了,退出");
//                    break;
//                }
                list = weiboResultEntity.getItems();
                if (!CollectionUtils.isEmpty(list)) {
                    boolean exist = false;
                    for (Status status : list) {
                        exist = DaoFactory.weiboDao.existWeibo(Long.parseLong(status.getMid()));
                        if (!exist) {
                            status.setBeidou_id(keyword.getBeidou_id());
                            status.setBeidou_name(keyword.getEvent_name());
                            status.setType(ContentType.search.toString());
                            DaoFactory.weiboDao.saveWeibo(status);
                        }
                    }
                }
                page++;
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (Exception e) {
                page++;
                e.printStackTrace();
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args){
        WeiboSearchHistoryCrawlerThread history = new WeiboSearchHistoryCrawlerThread();
        history.start();
    }
}
