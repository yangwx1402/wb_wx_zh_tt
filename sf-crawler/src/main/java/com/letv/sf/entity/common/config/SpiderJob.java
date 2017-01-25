package com.letv.sf.entity.common.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/13.
 */
@XStreamAlias("job")
public class SpiderJob implements java.io.Serializable{

    @XStreamAsAttribute
    private boolean use;
    @XStreamAsAttribute
    private String group;
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String classname;

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    private List<SpiderJobParam> params;

    private SpiderJobTrigger trigger;

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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<SpiderJobParam> getParams() {
        return params;
    }

    public void setParams(List<SpiderJobParam> params) {
        this.params = params;
    }

    public SpiderJobTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(SpiderJobTrigger trigger) {
        this.trigger = trigger;
    }
}
