package com.letv.spider.config;

import org.springframework.context.annotation.*;

/**
 * Created by yangyong3 on 2017/1/13.
 */
@Configuration
@ComponentScan("com.letv.spider")
@PropertySource({"classpath:config.properties"})
public class SpiderConfig {
}
