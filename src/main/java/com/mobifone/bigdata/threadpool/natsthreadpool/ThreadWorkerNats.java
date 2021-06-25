package com.mobifone.bigdata.threadpool.natsthreadpool;

import com.mobifone.bigdata.util.StreamingUtils;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class ThreadWorkerNats implements Runnable {
    private long TTL;
    private Table tableSYS,tableMDO;
    private List<String> arrList;

    public ThreadWorkerNats(long TTL, Table tableSYS, Table tableMDO, List<String> arrList) {
        this.TTL = TTL;
        this.tableSYS = tableSYS;
        this.tableMDO = tableMDO;
        this.arrList = arrList;
    }

    @Override
    public void run() {
        Utils utilHbase = Utils.getInstance();
        try {
            Connection connection = utilHbase.GetConnectionHbase();
            new StreamingUtils().MappingDataNat(arrList,tableMDO,tableSYS,utilHbase,TTL);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
