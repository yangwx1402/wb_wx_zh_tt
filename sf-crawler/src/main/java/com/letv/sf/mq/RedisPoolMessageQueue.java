package com.letv.sf.mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/19.
 */
public class RedisPoolMessageQueue extends RedisMessageQueue implements MessageQueue {

    public boolean pub(String queueName, Object obj, MessagePriority priority) throws IOException {
        return super.pub(queueName, obj, false);
    }

    @Override
    public boolean pub(String queueName, List<Object> obj, MessagePriority priority) throws IOException {
        return super.pub(queueName, obj, false);
    }

    @Override
    public <T> List<T> sub(String queueName, Class<T> tClass, int num) throws IOException {
        List<T> list = new ArrayList<T>();
        T t = null;
        for (int i = 0; i < num; i++) {
            t = sub(queueName, tClass);
            if (t != null)
                list.add(t);
        }
        return list;
    }

    public <T> T sub(String queueName, Class<T> tClass) throws IOException {
        return super.sub(queueName, tClass, false);
    }

    public long size(String queueName) throws IOException {
        return super.size(queueName, false);
    }

    public void clear(String queueName) throws IOException {
        super.clear(queueName, false);
    }

    public void destory() {
        super.destory();
        log.info("destory " + RedisPoolMessageQueue.class.getName() + " jedis resource " + new Date());
    }

}
