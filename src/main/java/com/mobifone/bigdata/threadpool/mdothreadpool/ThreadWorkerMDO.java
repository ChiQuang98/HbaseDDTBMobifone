package com.mobifone.bigdata.threadpool.mdothreadpool;

import com.mobifone.bigdata.util.StreamingUtils;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class ThreadWorkerMDO implements Runnable {
    private long TTL;
    private Table tableMDO;
    private List<String> arrList;

    public ThreadWorkerMDO(long TTL, Table tableMDO, List<String> arrList) {
        this.TTL = TTL;
        this.tableMDO = tableMDO;
        this.arrList = arrList;
    }

    @Override
    public void run() {
        Utils utilHbase = Utils.getInstance();
        try {
            Connection connection = utilHbase.GetConnectionHbase();
            new StreamingUtils().writeDataMDO(arrList,tableMDO,utilHbase,TTL);
        } catch (IOException | ParseException e) {
            //e.printStackTrace();
        }

    }
}
