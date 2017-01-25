package com.letv.sf.mq;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.config.SpiderMessagePool;
import com.letv.sf.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/11/29.
 */
public abstract class RedisMessageQueue implements MessageQueue {

    protected static final Logger log = Logger.getLogger(RedisMessageQueue.class);

    protected SpiderMessagePool queueConfig;

    private int capacity;

    public void setQueueConfig(SpiderMessagePool messagePool) {
        this.queueConfig = messagePool;
        this.capacity = queueConfig.getPoolSize() == null ? 1000 : queueConfig.getPoolSize();
    }

    public boolean pub(String queueName, Object obj, boolean close) throws IOException {
        if (size(queueName) >= capacity()) {
            return false;
        }
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (jedis == null)
            return false;
        try {
            jedis.lpush(SpiderConfig.SPIDER_REDIS_PREFIX + queueName, JsonUtils.toJson(obj));
            log.info("pub now " + SpiderConfig.SPIDER_REDIS_PREFIX + queueName + " message pool is -" + size(queueName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null && close) {
                jedis.close();
            }
        }
        return false;
    }

    public boolean pub(String queueName, List<Object> objs, boolean close) throws IOException {
        if (CollectionUtils.isEmpty(objs))
            return true;
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (jedis == null)
            return false;
        String[] messages = new String[objs.size()];
        for (int i = 0; i < objs.size(); i++) {
            messages[i] = JsonUtils.toJson(objs.get(i));
        }
        try {
            jedis.lpush(SpiderConfig.SPIDER_REDIS_PREFIX + queueName, messages);
            log.info("pub now " + SpiderConfig.SPIDER_REDIS_PREFIX + queueName + " message pool is -" + size(queueName));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null && close) {
                jedis.close();
            }
        }
        return false;
    }

    public <T> T sub(String queueName, Class<T> tClass, boolean close) throws IOException {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (jedis == null)
            return null;
        try {
            String value = jedis.rpop(SpiderConfig.SPIDER_REDIS_PREFIX + queueName);
            if (!StringUtils.isEmpty(value)) {
                log.info("sub now " + SpiderConfig.SPIDER_REDIS_PREFIX + queueName + " message pool is -" + size(queueName));
                return JsonUtils.fromJson(value, tClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null && close)
                jedis.close();
        }
        return null;
    }
    public long size(String queueName, boolean close) throws IOException {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (jedis == null)
            return Integer.MAX_VALUE;
        try {
            return jedis.llen(SpiderConfig.SPIDER_REDIS_PREFIX + queueName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null && close)
                jedis.close();
        }
        return 0;
    }

    public void clear(String queueName, boolean close) throws IOException {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (jedis == null)
            return;
        try {
            jedis.del(SpiderConfig.SPIDER_REDIS_PREFIX + queueName);
            log.info("clear " + queueName + " now size is " + size(SpiderConfig.SPIDER_REDIS_PREFIX + queueName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null && close)
                jedis.close();
        }
    }

    public void destory() {
        if (RedisFactory.getInstance() != null)
            RedisFactory.getInstance().destory();
    }

    public int capacity() {
        return capacity;
    }
}
