package com.letv.sf.mq;

import java.io.IOException;

/**
 * Created by yangyong3 on 2017/1/9.
 */
public class BlockMessageQueueExample {
    public static void main(String[] args) throws IOException {
        MessageQueue messageQueue = new PriorityBlockingMessageQueue();
        messageQueue.pub("test", 1, MessagePriority.low);
        messageQueue.pub("test", new TestObject(), MessagePriority.middle);
        messageQueue.pub("test", new TestObject(), MessagePriority.high);

        TestObject obj = messageQueue.sub("test",TestObject.class);
        System.out.println(obj.getPriority());
    }
}
