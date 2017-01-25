package com.letv.sf.mq;

import com.letv.sf.entity.mq.PriorityEntity;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by yangyong3 on 2017/1/9.
 */
public class PriorityBlockingMessageQueue extends BlockingMessageQueue {

    @Override
    public BlockingQueue CreateQueue(String queueName) {
        return new PriorityBlockingQueue(queue_size);
    }

    @Override
    public boolean check(String queueName, Object obj, MessagePriority priority) throws IOException {
        if(obj instanceof PriorityEntity)
            return true;
        else
           throw new IOException("放入队列的数据必须实现PriorityEntity抽象类");
    }

    @Override
    public synchronized boolean pub(String queueName, Object obj, MessagePriority priority) throws IOException {
        check(queueName,obj,priority);
        PriorityEntity entity = (PriorityEntity)obj;
        entity.setPriority(priority);
        return super.pub(queueName, entity, priority);
    }
}
