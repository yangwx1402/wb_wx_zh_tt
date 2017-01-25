package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("trigger")
public class SpiderJobTrigger implements java.io.Serializable{

    @XStreamAsAttribute
    private String group;
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String cron;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
