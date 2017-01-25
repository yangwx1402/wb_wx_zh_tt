package com.letv.sf.entity.beidou;

import java.io.Serializable;

/**
 * Created by yangyong3 on 2016/12/14.
 */
public class BeidouReportLabel implements Serializable {
    private BeidouReport all;

    private BeidouReport add;

    public BeidouReport getAll() {
        return all;
    }

    public void setAll(BeidouReport all) {
        this.all = all;
    }

    public BeidouReport getAdd() {
        return add;
    }

    public void setAdd(BeidouReport add) {
        this.add = add;
    }

    public String toString(){
        return "all:"+all+";add:"+add;
    }
}
