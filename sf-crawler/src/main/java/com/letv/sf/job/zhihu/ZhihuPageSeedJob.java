package com.letv.sf.job.zhihu;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Set;

/**
 * Created by yangyong3 on 2016/12/27.
 */
public class ZhihuPageSeedJob extends ZhihuProcess implements Job {

    private Set<String> priority_pool = SpiderConfig.getSpiderCrawlerPriorityBeidous();

    public void execute(JobExecutionContext context) throws JobExecutionException {
        //找出需要爬取评论的知乎问题
        List<YuqingArticle> articles = findQestionNotCommented();
        if (CollectionUtils.isEmpty(articles))
            return;
        for (YuqingArticle article : articles) {
            try {
                if (priority_pool.contains(article.getBeidou_id() + ""))
                    MessageFactory.pub(SpiderConfig.getZhihuQuestionQueueName(), article, MessagePriority.high);
                else
                    MessageFactory.pub(SpiderConfig.getZhihuQuestionQueueName(), article, MessagePriority.low);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws JobExecutionException {
        ZhihuPageSeedJob job = new ZhihuPageSeedJob();
        job.execute(null);
    }
}
