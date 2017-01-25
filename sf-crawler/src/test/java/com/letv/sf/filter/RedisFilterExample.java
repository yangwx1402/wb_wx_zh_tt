package com.letv.sf.filter;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public class RedisFilterExample {
    public static void main(String[] args) {
        Filter filter = new RedisFilter();
        for(int i=0;i<100;i++){
            filter.add("user_"+i);
        }
        for(int i=0;i<10;i++){
            System.out.println(filter.exist("user_"+(i*20)));
        }
    }
}
