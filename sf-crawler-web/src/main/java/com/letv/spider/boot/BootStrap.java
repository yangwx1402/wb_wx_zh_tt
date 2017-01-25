package com.letv.spider.boot;

import com.letv.spider.config.SpiderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by yangyong3 on 2017/1/13.
 */
@SpringBootApplication
public class BootStrap {
    public static void main(String[] args){
        SpringApplication.run(SpiderConfig.class, args);
    }
}
