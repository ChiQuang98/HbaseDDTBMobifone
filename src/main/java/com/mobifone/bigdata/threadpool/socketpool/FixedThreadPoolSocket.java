package com.mobifone.bigdata.threadpool.socketpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolSocket {
    private int NUM_OF_THREAD;
    ExecutorService executorService;
    private static FixedThreadPoolSocket instance;
    public static FixedThreadPoolSocket getInstance(int numThread){
        if(instance==null){
            instance = new FixedThreadPoolSocket(numThread);
        }
        return instance;
    }
    public FixedThreadPoolSocket(int NUM_OF_THREAD) {
        this.NUM_OF_THREAD = NUM_OF_THREAD;
        executorService = Executors.newFixedThreadPool(NUM_OF_THREAD);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public int getNUM_OF_THREAD() {
        return NUM_OF_THREAD;
    }

    public void setNUM_OF_THREAD(int NUM_OF_THREAD) {
        this.NUM_OF_THREAD = NUM_OF_THREAD;
    }
}
