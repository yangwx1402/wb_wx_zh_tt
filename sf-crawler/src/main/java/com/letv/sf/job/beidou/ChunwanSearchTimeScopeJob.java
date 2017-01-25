package com.letv.sf.job.beidou;

import com.letv.sf.job.thread.weibo.KeywordSearchHistoryThread;
import com.letv.sf.mq.RedisFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by yangyong3 on 2017/1/22.
 */
public class ChunwanSearchTimeScopeJob {

    public static void pushTimeScope(String start, String end) {
        Jedis jedis = RedisFactory.getInstance().getJedis();
        jedis.set(KeywordSearchHistoryThread.WEIBO_HISTORY_START_TIME,start);
        jedis.set(KeywordSearchHistoryThread.WEIBO_HISTORY_END_TIME,end);
    }

    public static void main(String[] args) {
        String start = args[0];
        String end = args[1];
        pushTimeScope(start, end);
    }
}
