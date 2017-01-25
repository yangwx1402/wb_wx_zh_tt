package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("executors")
public class SpiderExecutor implements java.io.Serializable {
    @XStreamAsAttribute
    private Integer core;
    @XStreamAsAttribute
    private Integer max;
    @XStreamAsAttribute
    private Long idleminutes;
    @XStreamImplicit(itemFieldName = "thread")
    private List<SpiderThread> threads;

    public Integer getCore() {
        return core;
    }

    public void setCore(Integer core) {
        this.core = core;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Long getIdleminutes() {
        return idleminutes;
    }

    public void setIdleminutes(Long idleminutes) {
        this.idleminutes = idleminutes;
    }

    public List<SpiderThread> getThreads() {
        return threads;
    }

    public void setThreads(List<SpiderThread> threads) {
        this.threads = threads;
    }
}
