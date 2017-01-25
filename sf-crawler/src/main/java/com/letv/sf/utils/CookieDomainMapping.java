package com.letv.sf.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyong3 on 2017/1/11.
 */
public class CookieDomainMapping {
    public static final Map<String, String> mapping = new HashMap<String, String>();
    static{
        mapping.put("SCF","sina.com.cn");
        mapping.put("tgc","login.sina.com.cn");
        mapping.put("SUB","sina.com.cn");
        mapping.put("SUBP","sina.com.cn");
        mapping.put("ALC","login.sina.com.cn");
        mapping.put("ALF","sina.com.cn");
        mapping.put("LT","login.sina.com.cn");
        mapping.put("sso_info","sina.com.cn");
        mapping.put("cREMloginname","login.sina.com.cn");
        mapping.put("SUHB","weibo.com");
        mapping.put("SRT","passport.weibo.com");
        mapping.put("SRF","passport.weibo.com");
        mapping.put("SSOLoginState","weibo.com");
        mapping.put("U_TRS1","sina.com.cn");
        mapping.put("U_TRS2","sina.com.cn");
        mapping.put("WEB2_APACHE2_YZ","my.sina.com.cn");
        mapping.put("NSC_wjq_txfjcp_mjotij","s.weibo.com");
        mapping.put("bdshare_firstime","my.sina.com.cn");
        mapping.put("TUIJIAN","cre.mix.sina.com.cn");
        mapping.put("WEB3_PHP-FPM_BX","zhongce.sina.com.cn");
        mapping.put("PHPSESSID","zhongce.sina.com.cn");
        mapping.put("UOR","sina.com.cn");
    }
}
