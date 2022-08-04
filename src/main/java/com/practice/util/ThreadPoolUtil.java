package com.practice.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(11, 11,
            0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());

    public static boolean isTerminated = false;

    public static void shutdownExecutor() {
        isTerminated = true;
        executor.shutdown();
    }

    public static ThreadPoolExecutor getExecutor(){
        return executor;
    }
}
