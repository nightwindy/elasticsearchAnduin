package com.anduin.api.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolFactory {

    private static volatile ExecutorService thredPool = null;
    private static volatile ThreadPoolTaskExecutor asyncTaskExecutor = null;

    private static final int CORE_SIZE = 50;
    private static final int MAX_SIZE = 200;
    private static final int KEEP_ALIVE_TIME = 5;

    public static ExecutorService getThreadPool() {
        if (null != thredPool) {
            return thredPool;
        }
        synchronized (ThreadPoolFactory.class) {
            if (null != thredPool) {
                return thredPool;
            }
            thredPool = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(MAX_SIZE));
            return thredPool;
        }
    }

    public static ThreadPoolTaskExecutor getAsyncTaskExecutor() {
        if (null != asyncTaskExecutor) {
            return asyncTaskExecutor;
        }
        synchronized (ThreadPoolFactory.class) {
            if (null != asyncTaskExecutor) {
                return asyncTaskExecutor;
            }
            asyncTaskExecutor = new ThreadPoolTaskExecutor();
            asyncTaskExecutor.initialize();
            return asyncTaskExecutor;
        }
    }

    public static void shutdown() {
        thredPool.shutdown();
    }


}
