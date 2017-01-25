package com.letv.spider.utils;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangyong3 on 2016/12/4.
 * 爬虫线程池
 */
public class CrawlerThreadPool {

    private static final Logger log = Logger.getLogger(CrawlerThreadPool.class);

    private ThreadPoolExecutor threadPool;

    public CrawlerThreadPool(int core, int max, long alived, TimeUnit timeUnit, BlockingQueue<Runnable> queue) {
        this.threadPool = new ThreadPoolExecutor(core, max, alived, timeUnit, queue);
    }

    public CrawlerThreadPool() {
        this(100, 100, 1, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>());
    }

    public void execute(Runnable run) {
        threadPool.execute(run);
    }

    public void submit(Runnable run) {
        threadPool.submit(run);
    }

    public void shutdown(boolean now) {
        if (now)
            threadPool.shutdownNow();
        else
            threadPool.shutdown();
    }

    public void monitor(int wait, TimeUnit unit) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (true) {
            if (threadPool.awaitTermination(wait, unit)) {
                log.info("thread pool executor over cost time -" + (System.currentTimeMillis() - start));
                break;
            }
        }
    }
}
