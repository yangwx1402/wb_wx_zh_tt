package com.letv.sf.filter;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public class FilterFactory {

    private static Filter filter = new RedisFilter();

    public static boolean exist(String key) {
        return filter.exist(key);
    }

    public static void add(String key) {
        filter.add(key);
    }

}
