package com.letv.sf.utils;

/**
 * Created by yangyong3 on 2016/12/7.
 */
public class StringUtils {
    /**
     * 找出两个相同字符之间的内容
     * @param source
     * @param ch
     * @return
     */
    public static String findTwoCharContent(String source,String ch){
        int start = source.indexOf(ch);
        if(start<0)
            return null;
        int next = source.indexOf(ch,start+1);
        return source.substring(start,next+1);
    }
    public static void main(String[] args){
        String source = "#李晨##李晨霸天狼#@李晨注意休息[心][心][心][鲜花][鲜花][鲜花]";
       System.out.println(StringUtils.findTwoCharContent(source,"#"));
    }
}
