package com.letv.sf.mq;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.beidou.BeidouEntity;
import org.quartz.JobExecutionException;

import java.io.IOException;

/**
 * Created by yangyong3 on 2016/12/7.
 */
public class MessageQueueExample {
    public static void main(String[] args) throws JobExecutionException, IOException {
//        MoiveSearchWordPubJob job = new MoiveSearchWordPubJob();
//        job.execute(null);
//        MessageFactory.print(SpiderConfig.getKeyWordQueueName());
        for(int i=0;i<50;i++){
            BeidouEntity entity = MessageFactory.sub(SpiderConfig.getZhihuKeyWordQueueName(), BeidouEntity.class);
            System.out.println(entity);
            System.out.println(MessageFactory.size(SpiderConfig.getKeyWordQueueName()));
        }

        System.out.println(MessageFactory.size(SpiderConfig.getKeyWordQueueName()));
    }
}
