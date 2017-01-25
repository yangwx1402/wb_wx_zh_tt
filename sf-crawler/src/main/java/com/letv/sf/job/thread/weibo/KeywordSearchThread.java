package com.letv.sf.job.thread.weibo;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.beidou.BeidouEntity;
import com.letv.sf.mq.MessageFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangyong3 on 2016/12/1.
 * 多线程抓取搜索微博信息。
 */
public class KeywordSearchThread extends BaseKeywordSearcher implements Runnable {

    private static final Logger log = Logger.getLogger(KeywordSearchThread.class);

    private static final long ONE_HOUR = 1000 * 60 * 60;

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");

    public void run() {
        while (true) {
            try {
                BeidouEntity keyword = MessageFactory.sub(SpiderConfig.getKeyWordQueueName(), BeidouEntity.class);
                if (keyword != null) {
                    String regionScope = getRegionScope(keyword);
                    String timeScope = getTimeScope();
                    process(keyword, regionScope, timeScope);
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

    public String getRegionScope(BeidouEntity keyword) {
        return super.getRegionScope(keyword);
    }

    public String getTimeScope() {
        if (SpiderConfig.getWeiboSearchTimeIntervalHour() != 0) {
            long now = System.currentTimeMillis();
            long after = now + ONE_HOUR;
            long before = now - ONE_HOUR * SpiderConfig.getWeiboSearchTimeIntervalHour();
            return "&timescope=custom:" + format.format(new Date(before)) + ":" + format.format(new Date(after));
        }
        return "";
    }


}
