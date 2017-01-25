package com.letv.sf.reflect;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.mq.MessageQueue;

/**
 * Created by yangyong3 on 2016/12/21.
 */
public class ReflectExample {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MessageQueue messageQueue = (MessageQueue) Class.forName("com.letv.sf.mq.RedisPoolMessageQueue").newInstance();
        System.out.println(SpiderConfig.getSleepTimes(10));
    }
}
