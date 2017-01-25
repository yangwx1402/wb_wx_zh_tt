package com.letv.sf.mq;

import java.io.IOException;
import java.util.Date;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Created by yangyong3 on 2016/12/21.
 */
public class PriorityRedisMessageQueue extends RedisPoolMessageQueue {

    private final TreeSet<MessagePriority> cache = new TreeSet<MessagePriority>();

    public PriorityRedisMessageQueue(){
        if (cache.size() == 0) {
            cache.add(MessagePriority.high);
            cache.add(MessagePriority.middle);
            cache.add(MessagePriority.low);
        }
    }

    private <T> T findMessage(String queueName, Class<T> tClass, MessagePriority from) throws IOException {

        if (from == null)
            from = cache.first();
        T t = super.sub(queueName + "_" + from.getName(), tClass,false);
        NavigableSet<MessagePriority> tail = null;
        if (t == null) {
            tail = cache.tailSet(from, false);
            if (tail == null || tail.size() == 0)
                return null;
            from = tail.first();
            return findMessage(queueName, tClass, from);
        } else {
            log.info("sub now " + queueName + " message pool is -" + size(queueName+"_"+from.getName()));
            return t;
        }
    }

    @Override
    public boolean pub(String queueName, Object obj, MessagePriority priority) throws IOException {
        return super.pub(queueName + "_" + priority.getName(), obj, false);
    }

    @Override
    public <T> T sub(String queueName, Class<T> tClass) throws IOException {
        return findMessage(queueName, tClass, null);
    }

    @Override
    public long size(String queueName) throws IOException {
        return super.size(queueName);
    }

    @Override
    public void clear(String queueName) throws IOException {
        for (MessagePriority priority : cache) {
            super.clear(queueName + "_" + priority.getName());
        }
    }

    @Override
    public void destory() {
        super.destory();
        log.info("destory "+PriorityRedisMessageQueue.class.getName()+" time is "+new Date());
    }
}
