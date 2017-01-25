package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by yangyong3 on 2016/12/23.
 */
@XStreamAlias("redisConfig")
public class RedisConfig implements java.io.Serializable {

    @XStreamAsAttribute
    private String ip;
    @XStreamAsAttribute
    private Integer port;
    @XStreamAsAttribute
    private String auth;
    @XStreamAsAttribute
    private Integer max;
    @XStreamAsAttribute
    private Integer min;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }
}
