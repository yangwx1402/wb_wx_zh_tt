package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("param")
public class SpiderJobParam {

    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
