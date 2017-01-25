package com.letv.sf.job.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.entity.weibo.WeiboTrace;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/2.
 * 跟踪一段时间内的微博热度
 */
@DisallowConcurrentExecution
public class WeiboTraceCrawlerJob extends BaseWeiboTrace implements Job {
    private static final int default_seed = 10;
    private static final Logger log = Logger.getLogger(WeiboTraceCrawlerJob.class);

    private static final int WEIBO_TRACE_PAGE_SIZE = 100;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            //获取最多1000000条微博
            List<BeidouMapping> mids = getTraceEntitys();
            if (CollectionUtils.isEmpty(mids))
                return;
            //分页跟踪
            int total = mids.size();
            int index = total % WEIBO_TRACE_PAGE_SIZE == 0 ? total / WEIBO_TRACE_PAGE_SIZE : total / WEIBO_TRACE_PAGE_SIZE + 1;
            int from = 0;
            List<BeidouMapping> temp = null;
            for (int i = 0; i < index; i++) {
                if (from + WEIBO_TRACE_PAGE_SIZE > total) {
                    temp = mids.subList(from, total);
                } else {
                    temp = mids.subList(from, from + WEIBO_TRACE_PAGE_SIZE);
                    from = from + WEIBO_TRACE_PAGE_SIZE;
                }
                process(temp);
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private List<BeidouMapping> getTraceEntitys() throws IOException {
        return MessageFactory.sub(BaseWeiboTrace.WEIBO_TRACE_QUEUE_NAME, BeidouMapping.class, 100);
    }

    public static void main(String[] args) throws JobExecutionException {
        WeiboTraceCrawlerJob job = new WeiboTraceCrawlerJob();
        job.execute(null);
    }
}
