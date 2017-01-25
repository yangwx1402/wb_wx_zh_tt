package com.letv.sf.toutiao.crawler;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.utils.DateUtils;

import java.util.Date;

/**
 * Created by yangyong3 on 2017/1/6.
 */
public class Test {
    public static void main(String[] args){

        System.out.println("["+"\004"+"]");
        System.out.println(DateUtils.dateString(new Date(System.currentTimeMillis()-1482090333), SpiderConfig.insert_update_time_format));
    }
}
