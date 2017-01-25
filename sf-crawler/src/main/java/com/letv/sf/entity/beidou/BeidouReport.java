package com.letv.sf.entity.beidou;

import java.io.Serializable;

/**
 * Created by yangyong3 on 2016/12/14.
 */
public class BeidouReport implements Serializable{

    private String beidou_name;

    public String getBeidou_name() {
        return beidou_name;
    }

    public void setBeidou_name(String beidou_name) {
        this.beidou_name = beidou_name;
    }

    private String name;

    private Long num = 0l;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String toString(){
        return name+"="+num;
    }

}
