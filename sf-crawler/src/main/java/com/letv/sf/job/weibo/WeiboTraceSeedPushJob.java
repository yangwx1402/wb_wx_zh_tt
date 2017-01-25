package com.letv.sf.job.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.dao.DaoFactory;
import com.letv.sf.entity.weibo.BeidouMapping;
import com.letv.sf.mq.MessageFactory;
import com.letv.sf.mq.MessagePriority;
import com.letv.sf.mq.RedisFactory;
import com.letv.sf.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyong3 on 2017/1/24.
 */
@DisallowConcurrentExecution
public class WeiboTraceSeedPushJob implements Job {

    private static final String WEIBO_TRACE_QUEUE_NAME = "WEIBO_TRACE_QUEUE_NAME";

    private static final int max_num = 1000000;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<BeidouMapping> list = DaoFactory.weiboDao.getWeiboTraceId(max_num);
        if (CollectionUtils.isEmpty(list))
            return;
        List<BeidouMapping> temp = new ArrayList<BeidouMapping>();
        for (BeidouMapping beidouMapping : list) {
            temp.add(beidouMapping);
            try {
                if (temp.size() == 1000) {
                    putSeed(temp, WEIBO_TRACE_QUEUE_NAME, false);
                    temp.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean putSeed(List<BeidouMapping> mappings, String queueName, boolean close) {
        if (CollectionUtils.isEmpty(mappings))
            return true;
        Jedis jedis = RedisFactory.getInstance().getJedis();
        if (jedis == null)
            return false;
        String[] messages = new String[mappings.size()];
        try {
            for (int i = 0; i < mappings.size(); i++) {
                messages[i] = JsonUtils.toJson(mappings.get(i));
            }
            jedis.lpush(SpiderConfig.SPIDER_REDIS_PREFIX + queueName+"_"+MessagePriority.high, messages);
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

    public static void main(String[] args) throws JobExecutionException {
        WeiboTraceSeedPushJob job = new WeiboTraceSeedPushJob();
        job.execute(null);
    }
}
