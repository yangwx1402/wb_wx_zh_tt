package com.letv.sf.job.common;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.common.CrawlerSlotConfig;
import com.letv.sf.mq.RedisFactory;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by yangyong3 on 2017/1/20.
 */
public class SedHttpToolRedisCommanderJob implements Job {

    private static final String REDIS_QUEUE_PREFIX = SpiderConfig.SPIDER_REDIS_PREFIX+"HTTPTOOL_REDIS_QUEUE_";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<CrawlerSlotConfig> slots = DaoFactory.weiboSlotDao.getAllSlot();
        sendRedisCommand(slots);
    }

    private void sendRedisCommand(List<CrawlerSlotConfig> slots) {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (CollectionUtils.isEmpty(slots))
            return;
        for (CrawlerSlotConfig config : slots) {
            jedis.lpush(REDIS_QUEUE_PREFIX + config.getType(), config.getId() + "");
        }
    }

}
