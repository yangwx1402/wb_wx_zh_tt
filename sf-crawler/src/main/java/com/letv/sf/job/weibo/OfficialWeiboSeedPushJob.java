package com.letv.sf.job.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.config.ContentType;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import com.letv.sf.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/22.
 */
@DisallowConcurrentExecution
public class OfficialWeiboSeedPushJob implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<BeidouEntity> high = highBeidouEntity();
        pubMessage(high,MessagePriority.high);
        List<BeidouEntity> low = getLowBeidouEntity();
        pubMessage(low,MessagePriority.low);

    }

    private void pubMessage(List<BeidouEntity> beidouEntities, MessagePriority priority){
        if(!CollectionUtils.isEmpty(beidouEntities)){
            for(BeidouEntity beidou:beidouEntities) {
                try {
                    MessageFactory.pub(SpiderConfig.getWeiboOfficialQueueName(), beidou,priority);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<BeidouEntity> highBeidouEntity() {
        List<BeidouEntity> entities = DaoFactory.beidouDao.getKeywords(ContentType.official,null);
        return entities;
    }

    public List<BeidouEntity> getLowBeidouEntity(){
        return DaoFactory.beidouMongoDao.getMoiveOfficial(DateUtils.dateString(new Date(),SpiderConfig.insert_update_time_format));
    }
    public static void main(String[] args) throws JobExecutionException {
        OfficialWeiboSeedPushJob job = new OfficialWeiboSeedPushJob();
        job.execute(null);
    }
}
