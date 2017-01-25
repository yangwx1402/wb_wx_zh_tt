package com.letv.sf.mq;

import com.letv.sf.entity.common.config.SpiderMessagePool;

import java.io.IOException;

/**
 * Created by yangyong3 on 2016/12/21.
 */
public class ProiorityMessageQueueTest {
    public static void main(String[] args) throws IOException {
        SpiderMessagePool messagePool = new SpiderMessagePool();
        messagePool.setPoolSize(10);
        messagePool.setClassname(PriorityRedisMessageQueue.class.getName());

        MessageQueue messageQueue = new PriorityRedisMessageQueue();
        messageQueue.setQueueConfig(messagePool);
        for(int i=0;i<20;i++) {
            System.out.println(messageQueue.pub("test", "test_"+i,MessagePriority.high));
        }
        for(int i=0;i<20;i++) {
            System.out.println(messageQueue.pub("test", "test_"+i,MessagePriority.low));
        }
        for(int i=0;i<30;i++){
            System.out.println(messageQueue.sub("test",String.class));
        }
    }
}
