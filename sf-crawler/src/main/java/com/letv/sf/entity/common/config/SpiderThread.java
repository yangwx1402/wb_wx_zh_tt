package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("thread")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "classname" })
public class SpiderThread implements java.io.Serializable {
    @XStreamAsAttribute
    private Integer count;
    @XStreamAsAttribute
    private String name;
    private String classname;
    private boolean use;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
