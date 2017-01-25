package com.letv.sf.mq;

import java.util.Random;
import java.util.TreeSet;

/**
 * Created by yangyong3 on 2016/12/21.
 */
public class TreeSetTest {
    public static void main(String[] args){
        TreeSet<MessagePriority> set = new TreeSet<MessagePriority>();
        set.add(MessagePriority.high);
        set.add(MessagePriority.low);
        set.add(MessagePriority.middle);
        System.out.println(set);
        System.out.println(set.first());
        System.out.println(set.tailSet(MessagePriority.high,false));

        System.out.println(1000*60*60*24);
        System.out.println(new Random().nextInt(1));
        System.out.println(new Random().nextInt(1));
        System.out.println(new Random().nextInt(1));
        System.out.println(new Random().nextInt(1));
        System.out.println(new Random().nextInt(1));
        System.out.println(new Random().nextInt(1));
    }
}
