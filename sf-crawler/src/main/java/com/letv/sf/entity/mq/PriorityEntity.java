package com.letv.sf.entity.mq;

import com.letv.sf.mq.MessagePriority;

/**
 * Created by yangyong3 on 2017/1/9.
 */
public abstract class PriorityEntity implements Comparable<PriorityEntity> {
    private MessagePriority priority;

    public MessagePriority getPriority() {
        return priority;
    }

    public void setPriority(MessagePriority priority) {
        this.priority = priority;
    }

    public int compareTo(PriorityEntity o) {
        return this.priority.compareTo(o.getPriority());
    }
}
