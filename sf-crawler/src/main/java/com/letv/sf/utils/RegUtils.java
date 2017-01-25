package com.letv.sf.utils;

/**
 * Created by yangyong3 on 2016/12/7.
 */
public class RegUtils {

    /**
     * 去掉文本中的特殊符号
     * @param source
     * @return
     */
    public static String wordNumChar(String source){
        return source.replaceAll("[^\u4e00-\u9fa5，。~`?？><！\\]\\[\\{\\}、 ……,#$%^&*!@.!a-zA-Z\\d]+","");
    }

    public static void main(String[] args){
      System.out.println(RegUtils.wordNumChar(" iPhone 7 Plus"));
    }
}
