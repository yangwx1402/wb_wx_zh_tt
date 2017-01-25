package com.letv.sf.mq;

import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.config.RedisConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yangyong3 on 2016/12/23.
 */
public class RedisFactory {

    private static final Logger log = Logger.getLogger(RedisFactory.class);

    private GenericObjectPoolConfig config;

    private String ip = "localhost";

    private int port = 6379;

    private String auth;

    private ReentrantLock lockPool = new ReentrantLock();

    private ReentrantLock lockJedis = new ReentrantLock();

    private JedisPool jedisPool;

    private ThreadLocal<Jedis> jedisCache = new ThreadLocal<Jedis>();

    private static RedisFactory instance;

    protected void init() {
        if (jedisPool != null)
            return;
        assert !lockPool.isHeldByCurrentThread();
        lockPool.lock();
        try {
            if (jedisPool == null) {
                config.setBlockWhenExhausted(false);
                jedisPool = new JedisPool(config, this.ip, this.port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockPool.unlock();
        }
    }

    public Jedis getJedis() {
        Jedis jedis = jedisCache.get();
        if (jedis != null) {
            if(!jedis.isConnected())
                jedis.connect();
            log.info("get jedis from threadLocal finished jedis object  is " + jedis);
            return jedis;
        }
        init();
        assert !lockJedis.isHeldByCurrentThread();
        lockJedis.lock();
        try {
            jedis = jedisPool.getResource();
            if (!StringUtils.isBlank(auth))
                jedis.auth(auth);
            log.info("get jedis from pool finished jedis object  is " + jedis);
            jedisCache.set(jedis);
            log.info("set jedis to threadLocal finished jedis object  is " + jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockJedis.unlock();
        }

        return jedis;
    }

    public RedisFactory(GenericObjectPoolConfig config, String ip, int port, String auth) {
        this.config = config;
        this.ip = ip;
        this.port = port;
        this.auth = auth;
    }

    public RedisFactory() {
        RedisConfig xmlConfig = SpiderConfig.getSpiderConfig().getRedisConfig();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        if (config != null) {
            this.ip = xmlConfig.getIp() == null ? "localhost" : xmlConfig.getIp();
            this.port = xmlConfig.getPort() == null ? 6739 : xmlConfig.getPort();
            this.auth = xmlConfig.getAuth();
            config.setMaxTotal(xmlConfig.getMax() == null ? 10 : xmlConfig.getMax());
            config.setMinIdle(xmlConfig.getMin() == null ? 5 : xmlConfig.getMin());
            this.config = config;
        }
    }

    public static RedisFactory getInstance(){
        if(instance == null)
            instance = new RedisFactory();
        return instance;
    }

    public void destory() {
        if (jedisPool != null)
            jedisPool.close();
        log.info("destory redis resource time is "+new Date());
    }
}
