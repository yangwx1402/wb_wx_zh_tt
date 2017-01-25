package com.letv.sf.job.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import com.letv.sf.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yangyong3 on 2016/12/7.
 * 产生weibo评论抓取seed并pub到队列中
 */
@DisallowConcurrentExecution
public class WeiboCommentSeedPubJob implements Job {

    private Set<String> priority_pool = SpiderConfig.getSpiderCrawlerPriorityBeidous();

    private static final Logger log = Logger.getLogger(WeiboCommentSeedPubJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<BeidouMapping> mids = getMids();
        if (CollectionUtils.isEmpty(mids))
            return;
        try {
            MessageFactory.clear(SpiderConfig.getWeiboCommentQueueName());
            log.info("clear "+SpiderConfig.getWeiboCommentQueueName()+" queue 成功 ");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("clear "+SpiderConfig.getWeiboCommentQueueName()+" queue 失败 ",e);
        }
        for (BeidouMapping mid : mids) {
            try {
                //需要优先抓取的评论,优先级为high
                if (priority_pool.contains(mid.getBeidou_id()))
                    MessageFactory.pub(SpiderConfig.getWeiboCommentQueueName(), mid, MessagePriority.high);
                //其他的优先级为low
                else
                    MessageFactory.pub(SpiderConfig.getWeiboCommentQueueName(), mid, MessagePriority.low);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<BeidouMapping> getMids() {
        List<BeidouMapping> mids = null;
        try {
            mids = DaoFactory.weiboDao.getCommentMids(SpiderConfig.getWeiboCommentFetchThreshold(), 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mids;
    }

    public static void main(String[] args) throws JobExecutionException {
        WeiboCommentSeedPubJob job = new WeiboCommentSeedPubJob();
        job.execute(null);
    }
}
