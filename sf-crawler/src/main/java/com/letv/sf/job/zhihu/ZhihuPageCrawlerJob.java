package com.letv.sf.job.zhihu;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.YuqingState;
import com.letv.sf.config.YuqingType;
import com.letv.sf.crawler.CrawlerFactory;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.yuqing.mapper.YuqingCommentDocMapper;
import com.letv.sf.dao.yuqing.mapper.YuqingDocMapper;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.entity.common.CrawlerResultEntity;
import com.letv.sf.entity.yuqing.YuqingArticle;
import com.letv.sf.entity.zhihu.ZhihuQuestionEntity;
import com.letv.sf.http.HttpResult;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.parser.ParseFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/12.
 * 知乎问题页爬取任务
 */
@DisallowConcurrentExecution
public class ZhihuPageCrawlerJob extends ZhihuProcess implements Job {

    private static final Logger log = Logger.getLogger(ZhihuPageCrawlerJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        //找出需要爬取评论的知乎问题
        try {
            YuqingArticle article = MessageFactory.sub(SpiderConfig.getZhihuQuestionQueueName(), YuqingArticle.class);
            if (article != null)
                process(article);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws JobExecutionException {
        ZhihuPageCrawlerJob job = new ZhihuPageCrawlerJob();
        job.execute(null);
    }
}
