package com.letv.sf.mq;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.config.SpiderMessagePool;
import com.letv.sf.entity.common.config.SpiderRoot;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by yangyong3 on 2016/12/1.
 * 消息队列工厂
 */
public class MessageFactory {

    private static MessageQueue queue;

    private static final Logger log = Logger.getLogger(MessageFactory.class);

    static{
        SpiderRoot root = SpiderConfig.getSpiderConfig();
        SpiderMessagePool messagePool = root.getMessagePool();
        if (messagePool != null) {
            try {
                queue = (MessageQueue) Class.forName(messagePool.getClassname()).newInstance();
                queue.setQueueConfig(messagePool);
                log.info("init message pool classname =[" + messagePool.getClassname() + "],poolsize=[" + messagePool.getPoolSize() + "]");
            } catch (Exception e) {
                e.printStackTrace();
                queue = new NormalBlockingMessageQueue();
                queue.setQueueConfig(messagePool);
                log.error("init message pool error change default messagepool classname =[" + BlockingMessageQueue.class + "],poolsize=[" + 1000 + "]",e);
            }
        }
    }


    public synchronized static void pub(String queueName, Object object,MessagePriority priority) throws IOException {
        queue.pub(queueName, object,priority);
    }

    public synchronized static void pub(String queueName, List<Object> objects, MessagePriority priority) throws IOException {
        queue.pub(queueName, objects,priority);
    }

    public synchronized static <T> T sub(String queueName, Class<T> tClass) throws IOException {
        T t = queue.sub(queueName, tClass);
        return t;
    }

    public synchronized static <T> List<T> sub(String queueName, Class<T> tClass,int num) throws IOException {
        return queue.sub(queueName,tClass,num);
    }

    public synchronized static long size(String queueName) throws IOException {
        return queue.size(queueName);
    }

    public synchronized static void clear(String queueName) throws IOException {
        queue.clear(queueName);

    }

    public static int capacity(String queueName){
        return queue.capacity();
    }

    public static void destory(){
              queue.destory();
    }

}
