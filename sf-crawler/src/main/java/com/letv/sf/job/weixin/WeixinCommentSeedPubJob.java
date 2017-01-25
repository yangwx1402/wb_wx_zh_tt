package com.letv.sf.job.weixin;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yangyong3 on 2016/12/13.
 * 微信网页seed生成任务
 */
@DisallowConcurrentExecution
public class WeixinCommentSeedPubJob implements Job {

    private Set<String> priority_pool = SpiderConfig.getSpiderCrawlerPriorityBeidous();

    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<YuqingArticle> articles = getNoFetchedArticles();
        if (CollectionUtils.isEmpty(articles))
            return;
        for (YuqingArticle article : articles) {

            //这里可以根据春晚和电影的beidou_id判断不同的优先级,春晚的为high,其他为low
            try {
                if (priority_pool.contains(article.getBeidou_id() + ""))
                    MessageFactory.pub(SpiderConfig.getWeixinCommentSeedQueueName(), article, MessagePriority.high);
                else
                    MessageFactory.pub(SpiderConfig.getWeixinCommentSeedQueueName(), article, MessagePriority.low);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<YuqingArticle> getNoFetchedArticles() {
        return DaoFactory.yuqingDao.getYuqingArticleByState(YuqingState.NO_FETCH_COMMENT.getState(), YuqingType.WEIXIN.getName(), 10000, new YuqingDocMapper());
    }
    public static void main(String[] args) throws JobExecutionException {
        WeixinCommentSeedPubJob job = new WeixinCommentSeedPubJob();
        job.execute(null);
    }
}
