package com.letv.sf.bootstrap;

import com.letv.sf.bootstrap.hook.ExitHookThread;
import com.letv.sf.config.SpiderConfig;
import com.letv.sf.entity.common.config.*;
import com.letv.sf.job.beidou.ChunwanSearchWordPubJob;
import com.letv.sf.job.beidou.MoiveSearchWordPubJob;
import com.letv.sf.job.quartz.QuartzScheduler;
import com.letv.sf.job.quartz.QuartzTriggerFactory;
import com.letv.sf.job.thread.CrawlerThreadPool;
import com.letv.sf.job.weibo.WeiboCommentSeedPubJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangyong3 on 2016/12/4.
 */
public class SpiderBootStrap {

    private static final Logger log = Logger.getLogger(SpiderBootStrap.class);

    private static CrawlerThreadPool threadPool;

    /**
     * 启动线程池
     *
     * @param executor spider线程池配置对象,里面包含了所有的跟线程池相关的信息
     * @throws InterruptedException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void startThreadPool(SpiderExecutor executor) throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //获取配置文件里的线程配置信息
        List<SpiderThread> threads = executor.getThreads();
        if (CollectionUtils.isEmpty(threads))
            return;
        //线程池队列
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<Runnable>(1000);
        //创建了一个线程池
        if (threadPool == null)
            threadPool = new CrawlerThreadPool(executor.getCore(), executor.getMax(), executor.getIdleminutes(), TimeUnit.MINUTES, queue);
        Thread t = null;
        //根据配置文件启动相关的线程
        for (SpiderThread thread : threads) {
            if (thread.isUse()) {
                for (int i = 0; i < thread.getCount(); i++) {
                    t = new Thread((Runnable) Class.forName(thread.getClassname()).newInstance());
                    if (!org.apache.commons.lang.StringUtils.isBlank(thread.getName()))
                        t.setName(thread.getName() + "-" + i);
                    threadPool.execute(t);
                    log.info("start thread " + t.getName() + ",time is " + new Date());
                }
            }
        }
    }

    /**
     * 启动quartz定时任务
     *
     * @param jobs
     * @throws SchedulerException
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void startQuartzJob(List<SpiderJob> jobs) throws SchedulerException, ParseException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //首先启动调度器
        QuartzScheduler.startSchedule();
        if (CollectionUtils.isEmpty(jobs))
            return;
        for (SpiderJob job : jobs) {
            if (job.isUse()) {
                JobDetail detail = QuartzScheduler.getJobDetail(job.getGroup(), job.getName(), Class.forName(job.getClassname()), getParams(job.getParams()));
                Trigger trigger = QuartzTriggerFactory.getCronTrigger(job.getTrigger().getGroup(), job.getTrigger().getName(), job.getTrigger().getCron());
                QuartzScheduler.addJob(detail, trigger);
            }
        }

    }

    /**
     * 获取配置文件中的job参数
     *
     * @param params
     * @return
     */
    private Map<String, Object> getParams(List<SpiderJobParam> params) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (CollectionUtils.isEmpty(params))
            return null;
        for (SpiderJobParam param : params) {
            result.put(param.getName(), param.getValue());
        }
        return result;
    }

    /**
     * spider启动主函数
     *
     * @throws ParseException
     * @throws SchedulerException
     * @throws InterruptedException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public void start() throws ParseException, SchedulerException, InterruptedException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        SpiderRoot configRoot = SpiderConfig.getSpiderConfig();
        init();
        startQuartzJob(configRoot.getJobs());
        startThreadPool(configRoot.getExecutors());
    }

    /**
     * 启动前的初始化
     *
     * @throws JobExecutionException
     */
    private void init() throws JobExecutionException {
        ChunwanSearchWordPubJob job = new ChunwanSearchWordPubJob();
        job.execute(null);
        WeiboCommentSeedPubJob job1 = new WeiboCommentSeedPubJob();
        job1.execute(null);
    }

    public static void main(String[] args) throws ParseException, InterruptedException, SchedulerException, IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        long start = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new ExitHookThread(threadPool,start));
        SpiderBootStrap bootStrap = new SpiderBootStrap();
        bootStrap.start();
    }
}
