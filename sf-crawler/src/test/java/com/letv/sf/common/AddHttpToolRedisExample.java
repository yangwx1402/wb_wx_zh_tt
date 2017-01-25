package com.letv.sf.common;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.mq.RedisFactory;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by yangyong3 on 2017/1/22.
 */
public class AddHttpToolRedisExample {

    private static final String REDIS_QUEUE_PREFIX = SpiderConfig.SPIDER_REDIS_PREFIX+"HTTPTOOL_REDIS_QUEUE_";

    public static void main(String[] args) {
        int start = 545;
        int end = 634;
        CrawlerSlotConfig config = null;
        Jedis jedis = RedisFactory.getInstance().getJedis();
        for(int i=start;i<=end;i++){
            config = DaoFactory.weiboSlotDao.getSlotById(i);
            if(config == null)
                continue;
            if(!StringUtils.isBlank(config.getCookie())&&config.getType().equals("weibo")){
                jedis.lpush(REDIS_QUEUE_PREFIX + config.getType(), config.getId() + "");
            }
        }
        jedis.close();
    }
}
