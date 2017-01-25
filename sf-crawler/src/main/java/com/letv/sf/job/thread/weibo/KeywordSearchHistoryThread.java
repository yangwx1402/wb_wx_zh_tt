package com.letv.sf.job.thread.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.RedisFactory;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangyong3 on 2017/1/22.
 */
public class KeywordSearchHistoryThread extends BaseKeywordSearcher implements Runnable {

    private long start = 0;

    private long end = 0;

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final long interval = 1000 * 60 * 60;

    public static final String WEIBO_HISTORY_START_TIME = SpiderConfig.SPIDER_REDIS_PREFIX + "WEIBO_HISTORY_START_TIME";

    public static final String WEIBO_HISTORY_END_TIME = SpiderConfig.SPIDER_REDIS_PREFIX + "WEIBO_HISTORY_END_TIME";

    @Override
    protected String getRegionScope(BeidouEntity keyword) {
        return super.getRegionScope(keyword);
    }

    @Override
    protected String getTimeScope() {
        if (start == 0) {
            start = getTime(WEIBO_HISTORY_START_TIME);
        }
        if (end == 0) {
            end = getTime(WEIBO_HISTORY_END_TIME);
        }
        String timeScope = "&timescope=custom:" + format.format(new Date(start)) + ":" + format.format(new Date(start + interval));
        start += interval;
        return timeScope;
    }

    private long getTime(String key) {
        try {
            Jedis jedis = RedisFactory.getInstance().getJedis();
            String dateString = jedis.get(key);
            return format.parse(dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return (System.currentTimeMillis() - interval * 24);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                BeidouEntity keyword = MessageFactory.sub(SpiderConfig.getKeyWordQueueName(), BeidouEntity.class);
                if (keyword != null) {
                    String regionScope = getRegionScope(keyword);
                    while (start <= end) {
                        String timeScope = getTimeScope();
                        process(keyword, regionScope, timeScope);
                    }
                }else{
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(SpiderConfig.getSleepTimes(default_seed));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
