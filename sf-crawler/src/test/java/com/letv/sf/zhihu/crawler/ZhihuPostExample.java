package com.letv.sf.zhihu.crawler;

import com.letv.sf.http.HttpResult;
import com.letv.sf.http.JsoupClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2016/12/9.
 */
public class ZhihuPostExample {
    public static void main(String[] args) throws IOException {
        String cookieString = "d_c0=\"AGCAzqem1AqPTobUcG3rILpXZ2Tdz98rhn0=|1478849458\"; _zap=8c60826d-a493-4ff2-b729-37eac8a0a003; r_cap_id=\"MjRmZTRmNDQzMzhhNDNiOGI2OTFhNmZlYjUzYTA2MWE=|1481252565|ec74d23d626c87e018b2b6f9fa36d39d4f84b62c\"; s-q=%E6%98%A5%E6%99%9A; s-i=3; sid=og4v7fvo; s-t=autocomplete; l_n_c=1; q_c1=c1f935959e214076816014c9abf97bbd|1481252596000|1481252596000; cap_id=\"Yjg0MTE3YjVlOGQ2NGJlNmE5MTQyODcxNjg3MzQ1NjE=|1481252596|980244b2efb026d67569e48de840e00773698d71\"; l_cap_id=\"YTY5ZmU3ZGEzYjYyNDRkZTkxNTNmZTNjODg1MDkwMTQ=|1481252596|eaf88f9257cdf1fadad2e98cefb49c61dcdb5a34\"; login=\"YTY4NDIxZmFkZGM4NDgzNzljNTRiZTc1ODM1M2E1YjA=|1481252944|dea3c14ec2bc4184c25a80dcab2e6657f74be068\"; _xsrf=d89f39e93fa32a98c9af047b39d2dfac; z_c0=Mi4wQUdCQWxNa3lPUW9BWUlET3A2YlVDaGNBQUFCaEFsVk5VSzF4V0FCYll1aU5OMkpwcThIajhiQTlUZHh4Rkg4YUhn|1481254407|a755d9c78d10da9b117c738d3049db865e670d08; __utma=51854390.1598484085.1478849438.1481249584.1481252490.8; __utmb=51854390.46.9.1481254058999; __utmc=51854390; __utmz=51854390.1481252490.8.7.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/question/28282827; __utmv=51854390.100--|2=registration_date=20160713=1^3=entry_date=20160713=1";
        Map<String,String> cookies = new HashMap<String, String>();
        String[] temp = cookieString.split(";");
        String[] temp1 = null;
        for(String line:temp){
          temp1 = line.split("=");
            cookies.put(temp1[0],temp1[1]);
        }

        JsoupClient client = new JsoupClient(null,cookies);
        String url = "https://www.zhihu.com/topic/20038208/hot";
        Map<String,String> map = new HashMap<String,String>();
        map.put("start","0");
        map.put("offset","2699.73439474");
        HttpResult result = client.post(url,map);
        System.out.println(result.getContent());
    }
}
