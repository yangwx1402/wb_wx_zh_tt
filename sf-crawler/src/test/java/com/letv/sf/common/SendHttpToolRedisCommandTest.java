package com.letv.sf.common;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.entity.common.HttpToolGroup;
import com.letv.sf.http.HttpToolFactory;
import com.letv.sf.mq.RedisFactory;
import org.apache.commons.collections.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by yangyong3 on 2017/1/20.
 */
public class SendHttpToolRedisCommandTest {

    private static final String REDIS_QUEUE_PREFIX = SpiderConfig.SPIDER_REDIS_PREFIX+"HTTPTOOL_REDIS_QUEUE_";

    public static void main(String[] args) throws InterruptedException {
//        List<CrawlerSlotConfig> slots = DaoFactory.weiboSlotDao.getSlotByType("weibo", 1);
//        Jedis jedis = RedisFactory.getInstance().getJedis();
//        if (CollectionUtils.isEmpty(slots))
//            return;
//        for (CrawlerSlotConfig config : slots) {
//            jedis.lpush(REDIS_QUEUE_PREFIX + config.getType(), config.getId() + "");
//        }
        HttpToolGroup group = HttpToolFactory.getHttpToolGroupByType("weibo");
        System.out.println(group);
        Thread.sleep(20000);
        HttpToolFactory.returnTool(group);
    }

}
