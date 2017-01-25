package com.letv.sf.entity.beidou;

import java.util.List;

/**
 * Created by yangyong3 on 2016/12/22.
 */
public class BeidouReportTable {

    private String[] fields;

    private List<Object[]> data;

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }
}
