package com.letv.sf.job.toutiao;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.toutiao.mapper.ToutiaoArticleMapper;
import com.letv.sf.entity.toutiao.ToutiaoArticle;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Set;

/**
 * Created by yangyong3 on 2017/1/5.
 */
public class ToutiaoCommentSeedPubJob implements Job {

    private Set<String> priority_pool = SpiderConfig.getSpiderCrawlerPriorityBeidous();

    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<ToutiaoArticle> articles = DaoFactory.toutiaoDao.fetchArticleByState(SpiderConfig.getSinaWeiboSearchReadyFetchComment(), new ToutiaoArticleMapper());
        if(CollectionUtils.isEmpty(articles))
            return;
        for(ToutiaoArticle article:articles){
            try{
                if(priority_pool.contains(article.getBeidou_id()+"")){
                    MessageFactory.pub(SpiderConfig.getToutiaoCommentQueueName(),article, MessagePriority.high);
                }else{
                    MessageFactory.pub(SpiderConfig.getToutiaoCommentQueueName(),article, MessagePriority.low);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws JobExecutionException {
        ToutiaoCommentSeedPubJob job = new ToutiaoCommentSeedPubJob();
        job.execute(null);
    }
}
