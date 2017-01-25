package com.letv.sf.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by yangyong3 on 2016/12/19.
 */
public class JedisExample {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, "10.183.222.210", 9008);
        Jedis jedis = pool.getResource();
        jedis.close();
        jedis.auth("sinx/cosx=tanx");
        //jedis.del("test");

        //jedis.set("test","test");
//        System.out.println(jedis.lpush("test","test1"));
//        System.out.println(jedis.lpush("test","test2"));
//
//        System.out.println(jedis.lpush("test","test3"));
//        System.out.println(jedis.llen("test"));
//        for (int i = 0; i < 10; i++)
//            System.out.println(jedis.rpop("test"));

         jedis.set("yangyong:sf-spider","test");
        jedis.close();
    }
}
