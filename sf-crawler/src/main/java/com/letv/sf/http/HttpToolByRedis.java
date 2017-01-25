package com.letv.sf.http;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.RedisFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xalan.xsltc.runtime.Hashtable;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/20.
 */
public class HttpToolByRedis extends AbstractHttpTool implements HttpTool{

    private static final Logger log = Logger.getLogger(HttpToolByRedis.class);

    private static final String REDIS_QUEUE_PREFIX = SpiderConfig.SPIDER_REDIS_PREFIX+"HTTPTOOL_REDIS_QUEUE_";

    private static final Map<String,HttpToolGroup> httpPool = new java.util.Hashtable<String,HttpToolGroup>();

    /**
     * 从redis队列中拿一个可用的HTTPTOOL
     * @param type
     * @return
     */
    @Override
    public HttpToolGroup getHttpToolGroupByType(String type) {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        HttpToolGroup group = null;
        CrawlerSlotConfig slot = null;
        String idString = jedis.rpop(REDIS_QUEUE_PREFIX+type);
        String key = type+"_"+idString;
        if(httpPool.containsKey(key)) {
            group = httpPool.get(key);
            log.info("get a httpPool from cache "+group);
            return group;
        }
        if(!StringUtils.isBlank(idString)){
            try {
                slot = DaoFactory.weiboSlotDao.getSlotById(Integer.parseInt(idString));
                group =  initSlot(slot);
                httpPool.put(key,group);
                log.info("init a httpgroup from mysql slot id is -"+slot.getId());
                return group;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    /**
     * 用完再放回到redis中
     * @param group
     */
    @Override
    public void returnTool(HttpToolGroup group) {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        jedis.lpush(REDIS_QUEUE_PREFIX+group.getType(),group.getId()+"");
    }

    @Override
    public void destory() {

    }

    @Override
    public int size(String type) {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        long count = jedis.llen(REDIS_QUEUE_PREFIX+type);
        return (int) count;
    }
}
