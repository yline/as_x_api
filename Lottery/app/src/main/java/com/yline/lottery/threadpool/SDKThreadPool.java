package com.yline.lottery.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池工具包
 */
public class SDKThreadPool {
    private static ExecutorService sFixedThreadPool;
    private static ExecutorService sSingleThreadPool;

    /**
     * 固定大小线程池，暂定5个
     */
    public static void fixedThreadExecutor(Runnable runnable) {
        if (sFixedThreadPool == null) {
            sFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        sFixedThreadPool.execute(runnable);
    }

    /**
     * 单线程池
     */
    public static void singleThreadExecutor(Runnable runnable) {
        if (sSingleThreadPool == null) {
            sSingleThreadPool = Executors.newSingleThreadExecutor();
        }
        sSingleThreadPool.execute(runnable);
    }

    public static <T> Future<T> fixedThreadExecutor(Callable<T> callable) {
        if (sFixedThreadPool == null) {
            sFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        return sFixedThreadPool.submit(callable);
    }
}
