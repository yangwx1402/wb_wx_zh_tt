package com.letv.sf.mq;

import com.letv.sf.entity.common.config.SpiderMessagePool;
import com.letv.sf.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by yangyong3 on 2016/11/29.
 * 基于Blockingqueue的消息队列
 */
public abstract class BlockingMessageQueue implements MessageQueue {


    private Map<String, BlockingQueue<Object>> queueCache = new Hashtable<String, BlockingQueue<Object>>();

    private static final Logger log = Logger.getLogger(BlockingMessageQueue.class);

    protected int queue_size = 1000;

    public abstract BlockingQueue CreateQueue(String queueName);

    public abstract boolean check(String queueName, Object obj, MessagePriority priority) throws IOException;

    public synchronized boolean pub(String queueName, Object obj, MessagePriority priority) throws IOException {
        boolean flag = false;
        BlockingQueue queue = null;
        if (!check(queueName, obj, priority)) {
            return flag;
        }
        if (size(queueName) >= capacity())
            return flag;
        if (queueCache.containsKey(queueName)) {
            flag = queueCache.get(queueName).offer(obj);
        } else {
            queue = CreateQueue(queueName);
            flag = queue.offer(obj);
            queueCache.put(queueName, queue);
        }
        log.info("pub now " + queueName + " message pool is -" + size(queueName));
        return flag;
    }

    @Override
    public boolean pub(String queueName, List<Object> obj, MessagePriority priority) throws IOException {
        if (CollectionUtils.isEmpty(obj))
            return true;
        for (Object o : obj)
            pub(queueName, o, priority);
        return true;
    }

    public synchronized <T> T sub(String queueName, Class<T> tClass) throws IOException {
        if (queueCache.containsKey(queueName)) {
            return (T) queueCache.get(queueName).poll();
        }
        log.info("sub now " + queueName + " message pool is -" + size(queueName));
        return null;
    }

    @Override
    public <T> List<T> sub(String queueName, Class<T> tClass, int num) throws IOException {
        List<T> list = new ArrayList<T>();
        T t = null;
        for (int i = 0; i < num; i++) {
            t = sub(queueName, tClass);
            if (t != null)
                list.add(sub(queueName, tClass));
        }
        return list;
    }

    public synchronized long size(String queueName) throws IOException {
        if (queueCache.containsKey(queueName)) {
            return queueCache.get(queueName).size();
        }
        return 0;
    }

    public void clear(String queueName) throws IOException {
        if (queueCache.containsKey(queueName)) {
            queueCache.get(queueName).clear();
        }
        log.info("clear " + queueName + " now size is " + size(queueName));
    }

    public void setQueueConfig(SpiderMessagePool messagePool) {
        this.queue_size = messagePool.getPoolSize();
    }

    public int capacity() {
        return queue_size;
    }

    public void destory() {
        if (queueCache != null && queueCache.size() > 0) {
            queueCache.clear();
        }
    }
}
