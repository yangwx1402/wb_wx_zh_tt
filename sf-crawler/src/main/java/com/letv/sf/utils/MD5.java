package com.letv.sf.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by yangyong3 on 2016/12/2.
 */
public class MD5 {
    public static String md5(String data){
       return DigestUtils.md5Hex(data);
    }

    public static void main(String[] args){
       System.out.println(md5("398901650@qq.com"));
    }
}
