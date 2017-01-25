package com.letv.sf.job.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTrace;
import com.letv.sf.http.HttpResult;
import com.letv.sf.parser.ParseFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by yangyong3 on 2017/1/24.
 */
public class BaseWeiboTrace {

    private static final Logger log = Logger.getLogger(BaseWeiboTrace.class);

    public static final String WEIBO_TRACE_QUEUE_NAME = "WEIBO_TRACE_QUEUE_NAME";

    protected void process(List<BeidouMapping> mids) throws Exception {
        StringBuffer sb = new StringBuffer();
        BeidouMapping beidouMapping = null;
        if (!CollectionUtils.isEmpty(mids)) {
            for (int i = 0; i < mids.size(); i++) {
                if (i == mids.size() - 1) {
                    sb.append(mids.get(i).getMid() + "");
                } else {
                    sb.append(mids.get(i).getMid() + ",");
                }
            }
            beidouMapping = mids.get(0);
            String url = SpiderConfig.getSinaWeiboTraceApiUrl() + "?ids=" + sb.toString();
            HttpResult<BeidouMapping> httpResult = CrawlerFactory.weiboTraceCrawler.crawl(url, null);
            if(httpResult == null){
                log.error("爬取url出错,url is -"+url+",statusCode = "+httpResult.getCode());
                return;
            }
            httpResult.setMeta(beidouMapping);
            CrawlerResultEntity<WeiboTrace> weiboTraceWeiboResultEntity = ParseFactory.weiboTraceParser.parse(httpResult);
            List<WeiboTrace> traces = weiboTraceWeiboResultEntity.getItems();
            if (!CollectionUtils.isEmpty(traces)) {
                DaoFactory.weiboDao.saveWeiboTrace(traces);
                DaoFactory.weiboDao.updateWeiboRCACount(traces);
            }
        }
    }
}
