package com.letv.sf.job.thread.weibo;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.counter.CounterTool;
import com.letv.sf.counter.CounterType;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.common.SpiderStatus;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import com.letv.sf.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import weibo4j.model.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2017/1/22.
 */
public abstract class BaseKeywordSearcher {

    private static final Logger log = Logger.getLogger(BaseKeywordSearcher.class);

    protected static final int default_seed = 10;

    protected void process(BeidouEntity keyword, String regionScope, String timeScope) {
        try {
            searchByKeyword(keyword, regionScope, timeScope);
            Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

    }

    protected String getRegionScope(BeidouEntity keyword) {
        if (!StringUtils.isEmpty(keyword.getRegion())) {
            return "&region=" + keyword.getRegion();
        }
        return "";
    }

    protected abstract String getTimeScope();

    protected void searchByKeyword(BeidouEntity keyword, String regionScope, String timeScope) {
        CrawlerResultEntity<Status> weiboResultEntity = null;
        String url = SpiderConfig.getSinaWeiboSearchUrl() + keyword.getTag() + SpiderConfig.getSinaWeiboSearchUrlSubffix();
        if (!StringUtils.isBlank(regionScope)) {
            url += regionScope;
        }
        if (!StringUtils.isBlank(timeScope)) {
            url += timeScope;
        }
        int page = 1;
        HttpResult result = null;
        weiboResultEntity = null;
        List<Status> list = null;
        //抓取到最大页数,不行就闪
        while (page != SpiderConfig.getSinaWeiboSearchMaxPage()) {
            try {
                result = CrawlerFactory.keyWordCrawler.crawl(url + "&page=" + page, null);
                CounterTool.count(CounterType.weibo_search_request_counter, 1);
                if (result == null)
                    break;
                weiboResultEntity = ParseFactory.keywordParser.parse(result);
                if (SpiderStatus.YANGZHENGMA == weiboResultEntity.getStatus()) {
                    log.info("beidou info -[" + JsonUtils.toJson(keyword) + "] fetch need 验证码 last url is " + (url + "&page=" + page));
                    break;
                }
                if (SpiderStatus.NO_RESULT == weiboResultEntity.getStatus()) {
                    log.info("beidou info -[" + JsonUtils.toJson(keyword) + "] fetch finished last url is " + (url + "&page=" + page));
                    break;
                }
                list = weiboResultEntity.getItems();
                List<Status> statuses = new ArrayList<Status>();
                if (!CollectionUtils.isEmpty(list)) {
                    boolean exist = false;
                    CounterTool.count(CounterType.weibo_search_counter, list.size());
                    for (Status status : list) {
                        exist = DaoFactory.weiboDao.existWeibo(Long.parseLong(status.getMid()));
                        if (!exist) {
                            status.setBeidou_id(keyword.getBeidou_id());
                            status.setBeidou_name(keyword.getEvent_name());
                            status.setType(ContentType.search.toString());
                            statuses.add(status);
                        }
                    }
                    //批量入库减少mysql压力
                    DaoFactory.weiboDao.saveWeibos(statuses);
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
}
