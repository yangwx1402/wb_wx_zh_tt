package com.letv.sf.crawler;

import java.util.Random;

/**
 * Created by yangyong3 on 2016/11/30.
 */
public class PatternExample {
    public static void main(String[] args){
      Random random = new Random();
        for(int i=0;i<10;i++){
            System.out.println(random.nextInt(2));
        }
    }
}
