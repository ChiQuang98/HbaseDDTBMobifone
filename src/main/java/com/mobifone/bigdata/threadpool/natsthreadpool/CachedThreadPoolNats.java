package com.mobifone.bigdata.threadpool.natsthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolNats {
    private static CachedThreadPoolNats instance;
    private ExecutorService executorService;
    public static CachedThreadPoolNats getInstance(){
        if (instance==null){
            instance = new CachedThreadPoolNats();
        }
        return instance;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public CachedThreadPoolNats() {
        this.executorService = Executors.newCachedThreadPool();
    }
}
