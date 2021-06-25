package com.mobifone.bigdata.threadpool.mdothreadpool;

import com.mobifone.bigdata.threadpool.natsthreadpool.CachedThreadPoolNats;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolMDO {
    private static CachedThreadPoolMDO instance;
    private ExecutorService executorService;
    public static CachedThreadPoolMDO getInstance(){
        if (instance==null){
            instance = new CachedThreadPoolMDO();
        }
        return instance;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public CachedThreadPoolMDO() {
        this.executorService = Executors.newCachedThreadPool();
    }
}
