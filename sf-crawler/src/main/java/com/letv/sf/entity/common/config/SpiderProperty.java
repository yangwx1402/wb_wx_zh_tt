package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import java.io.Serializable;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("property")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "value" })
public class SpiderProperty implements Serializable{

    @XStreamAsAttribute
    private String name;
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
