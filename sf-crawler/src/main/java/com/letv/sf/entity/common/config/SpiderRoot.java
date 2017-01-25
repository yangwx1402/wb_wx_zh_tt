package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("spider")
public class SpiderRoot implements java.io.Serializable {

    private List<SpiderProperty> properties;

    private SpiderMessagePool messagePool;

    private SpiderExecutor executors;

    private RedisConfig redisConfig;

    private List<SpiderJob> jobs;

    private List<SpiderMonitor> monitors;

    public List<SpiderProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<SpiderProperty> properties) {
        this.properties = properties;
    }

    public SpiderMessagePool getMessagePool() {
        return messagePool;
    }

    public void setMessagePool(SpiderMessagePool messagePool) {
        this.messagePool = messagePool;
    }

    public SpiderExecutor getExecutors() {
        return executors;
    }

    public void setExecutors(SpiderExecutor executors) {
        this.executors = executors;
    }

    public List<SpiderJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<SpiderJob> jobs) {
        this.jobs = jobs;
    }

    public List<SpiderMonitor> getMonitors() {
        return monitors;
    }

    public void setMonitors(List<SpiderMonitor> monitors) {
        this.monitors = monitors;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }
}
