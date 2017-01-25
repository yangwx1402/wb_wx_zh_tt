package com.letv.sf.job.beidou;

import com.letv.sf.config.ContentType;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.dao.beidou.BeidouDao;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;
import org.quartz.impl.JobExecutionContextImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by yangyong3 on 2016/12/28.
 */
@DisallowConcurrentExecution
public class ChunwanSearchWordPubJob extends SearchKeywordBase implements Job {
    private static final long interval = 1000 * 60 * 60;

    private static long pre_time = 0;

    private static final String[] DEFAULT_REGIONS = new String[]{"400", "100", "82", "81", "71", "65", "64", "63", "62", "61", "54", "53", "52", "51", "50", "46", "45", "44", "43", "42", "41", "37", "36", "35", "34", "33", "32", "31", "23", "22", "21", "15", "14", "13", "12", "11"};

    public void execute(JobExecutionContext context) throws JobExecutionException {
        String[] regions = new String[]{};
        if (context != null)
            regions = getRegions(context.getJobDetail().getJobDataMap());
        try {
            //跟春晚相关的微博搜索词pub到队列中,相应的优先级设置为high
            // pubKeywordToMessageQueue(DaoFactory.beidouDao, ContentType.search, SpiderConfig.getKeyWordQueueName(), false, MessagePriority.high);
            pubChunSearchWordToMessageQueue(DaoFactory.beidouDao, ContentType.search, SpiderConfig.getKeyWordQueueName(), false, MessagePriority.high, regions);
            //跟春晚相关的topic搜索词pub到队列中,相应的优先级为high
            pubKeywordToMessageQueue(DaoFactory.beidouDao, ContentType.topic, SpiderConfig.getTopicKeyWordQueueName(), false, MessagePriority.high);
            //跟春晚相关的微信搜索词pub到队列中,相应的优先级设置为high
            pubKeywordToMessageQueue(DaoFactory.beidouDao, ContentType.search, SpiderConfig.getWeixinXinKeyWordQueueName(), false, MessagePriority.high);
            //pubKeywordToMessageQueue(ContentType.search, SpiderConfig.getZhihuKeyWordQueueName(),false);
            if ((System.currentTimeMillis() - interval) > pre_time) {
                pubKeywordToMessageQueue(DaoFactory.beidouDao, ContentType.search, SpiderConfig.getToutiaoSearchKeywordQueueName(), false, MessagePriority.high);
                pre_time = System.currentTimeMillis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void pubChunSearchWordToMessageQueue(BeidouDao beidouDao, ContentType type, String queueName, boolean clear, MessagePriority priority, String[] regions) throws IOException {
        Date time = new Date();
        String date = DateUtils.dateString(time, SpiderConfig.insert_update_time_format);
        List<BeidouEntity> beidouEntities = beidouDao.getKeywords(type, date);
        if (clear)
            MessageFactory.clear(queueName);
        for (BeidouEntity entity : beidouEntities) {
            if(regions!=null&&regions.length>0) {
                for (String region : regions) {
                    entity.setRegion("custom:" + region + ":1000");
                    MessageFactory.pub(queueName, entity, priority);
                }
                MessageFactory.pub(queueName,entity,priority);
            }else{
                MessageFactory.pub(queueName, entity, priority);
            }
        }
    }

    private String[] getRegions(JobDataMap map) {
        String[] regions = null;
        if (map == null || !map.containsKey("region")) {
            regions = new String[]{};
        } else {
            regions = map.get("region").toString().split(",");
        }
        return regions;
    }

    public static void main(String[] args) throws JobExecutionException {
        ChunwanSearchWordPubJob job = new ChunwanSearchWordPubJob();
        job.execute(null);
    }

}
