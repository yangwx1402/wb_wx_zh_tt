package com.letv.sf.counter;

/**
 * Created by yangyong3 on 2017/1/13.
 */
public class CounterExample {
    public static void main(String[] args){
        CounterTool.count(CounterType.weibo_search_counter,1);
        CounterTool.count(CounterType.weibo_search_counter,10);
        System.out.println(CounterTool.print());
    }
}
