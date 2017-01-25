package com.letv.sf.filter;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public interface Filter {

    boolean exist(String key);

    void add(String key);
}
