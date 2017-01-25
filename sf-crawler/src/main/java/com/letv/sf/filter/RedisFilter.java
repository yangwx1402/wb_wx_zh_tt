package com.letv.sf.filter;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.mq.RedisFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public class RedisFilter implements Filter {

    private static final String REDIS_FILTER_SET_NAME = SpiderConfig.SPIDER_REDIS_PREFIX+"FILTER_SET_NAME";

    public boolean exist(String key) {
        try {
            Jedis jedis = RedisFactory.getInstance().getJedis();
            //这里说明缓存没有资源了,那么就返回false,也就是说不存在,同时采用mysql排重
            if (jedis == null)
                return false;
            return jedis.sismember(REDIS_FILTER_SET_NAME, key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void add(String key) {
        //这里把错误都屏蔽掉,不能影响正常的业务。
        try {
            Jedis jedis = RedisFactory.getInstance().getJedis();
            if (jedis == null)
                return;
            jedis.sadd(REDIS_FILTER_SET_NAME, key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
