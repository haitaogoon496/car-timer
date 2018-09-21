package com.mljr.heil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author : Guo LiXiao
 * @date : 2017-10-20  14:47
 */

@Configuration
public class BasicExecutorConfig {

    /** 初始线程池容量 **/
    private static final int EXE_CORE_POOL_SIZE = 4;

    /** 最大线程池容量 **/
    private static final int EXE_MAX_POOL_SIZE = 10;

    /** 线程池阻塞队列大小 **/
    private static final int EXE_QUEUE_CAPACITY = 1000;

    private static BlockingQueue<Runnable> blocking = new LinkedBlockingQueue<>(EXE_QUEUE_CAPACITY);

    @Bean(name = "basicExecutor", destroyMethod = "shutdown")
    public ExecutorService BasicExecutor() {
        /** 初始化线程池长度为 EXE_CORE_POOL_SIZE，线程池满以后新来的线程进入blocking，
         * blocking满以后线程池扩容，达到EXE_MAX_POOL_SIZE后，触发RejectedExecutionHandle规则**/
        ExecutorService executor = new ThreadPoolExecutor(EXE_CORE_POOL_SIZE, EXE_MAX_POOL_SIZE, 3000L, TimeUnit.MILLISECONDS, blocking, new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

    @Bean(name = "basicScheduler", destroyMethod="shutdown")
    public ScheduledExecutorService BasicScheduler(){
        ScheduledExecutorService scheduler =  new ScheduledThreadPoolExecutor(EXE_CORE_POOL_SIZE, new ThreadPoolExecutor.DiscardPolicy());
        return scheduler;
    }

}
