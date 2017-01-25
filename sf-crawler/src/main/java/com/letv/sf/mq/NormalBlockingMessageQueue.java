package com.letv.sf.mq;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yangyong3 on 2017/1/9.
 */
public class NormalBlockingMessageQueue extends BlockingMessageQueue {

    @Override
    public BlockingQueue CreateQueue(String queueName) {
        return new LinkedBlockingQueue(queue_size);
    }

    @Override
    public boolean check(String queueName, Object obj, MessagePriority priority) throws IOException {
        return true;
    }
}
