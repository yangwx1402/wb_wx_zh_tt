package com.letv.sf.job.beidou;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.mq.MessagePriority;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

/**
 * Created by young.yang on 2016/11/29.
 * 将关键词发布到分布式对列中,
 * 采用quarz定时发布keyword
 */
@DisallowConcurrentExecution
public class MoiveSearchWordPubJob extends SearchKeywordBase implements Job {

    /**
     * 将爬取关键词pub到队列中
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            //电影相关的微博搜索词pub到队列中,相应的优先级为low
            pubKeywordToMessageQueue(DaoFactory.beidouMongoDao, ContentType.movie,SpiderConfig.getKeyWordQueueName(),false,MessagePriority.low);
            //电影相关的微博topic搜索词pub到队列中,相应的优先级为low
            pubKeywordToMessageQueue(DaoFactory.beidouMongoDao, ContentType.movie,SpiderConfig.getTopicKeyWordQueueName(),false,MessagePriority.low);
            //电影相关的微信搜索词pub到队列中,相应的优先级设置为low
            pubKeywordToMessageQueue(DaoFactory.beidouMongoDao,ContentType.weixin,SpiderConfig.getWeixinXinKeyWordQueueName(),false,MessagePriority.low);
            //今日头条
            pubKeywordToMessageQueue(DaoFactory.beidouMongoDao,ContentType.movie,SpiderConfig.getToutiaoSearchKeywordQueueName(),false,MessagePriority.low);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JobExecutionException {
        MoiveSearchWordPubJob job = new MoiveSearchWordPubJob();
        job.execute(null);
    }
}
