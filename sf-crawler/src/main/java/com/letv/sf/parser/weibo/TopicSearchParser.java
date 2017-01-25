package com.letv.sf.parser.weibo;

import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerPageInfo;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.Parse;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

/**
 * Created by yangyong3 on 2016/11/30.
 * 微博话题搜索解析
 */
public class TopicSearchParser implements Parse<CrawlerResultEntity<Status>,BeidouEntity> {
    public CrawlerResultEntity<Status> parse(HttpResult httpPage) throws Exception {
        StatusWapper wapper = Status.constructWapperStatusJson(httpPage.getContent());
        CrawlerResultEntity<Status> result = new CrawlerResultEntity<Status>();
        result.setItems(wapper.getStatuses());
        CrawlerPageInfo page = new CrawlerPageInfo();
        page.setPageTotal(wapper.getTotalNumber());
        wapper.getTotalNumber();
        result.setPage(page);
        return result;
    }
}
