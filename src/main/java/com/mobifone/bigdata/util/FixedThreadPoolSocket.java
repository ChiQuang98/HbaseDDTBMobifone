package com.mobifone.bigdata.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolSocket {
    private static final int NUM_OF_THREAD = 5;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREAD);

    }
}
