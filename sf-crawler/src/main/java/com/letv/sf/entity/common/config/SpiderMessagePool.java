package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("messagePool")
public class SpiderMessagePool implements Serializable {

    @XStreamAsAttribute
    private Integer poolSize;
    @XStreamAlias("classname")
    private String classname;

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
    
}
