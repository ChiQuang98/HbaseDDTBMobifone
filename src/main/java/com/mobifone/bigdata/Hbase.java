package com.mobifone.bigdata;

import com.mobifone.bigdata.util.StreamingUtils;
import com.mobifone.bigdata.util.TCPSocketServer;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;


import java.io.IOException;

class HBase {
    private static final Logger logger = Logger.getLogger(HBase.class);
    public static void main(String[] args) throws IOException, Exception {
        try {
            Utils utilHbase = Utils.getInstance();
            StreamingUtils streamingUtils = StreamingUtils.getInstance();
            Connection connection = utilHbase.GetConnectionHbase();
            utilHbase.CreateTableHbase("MDOTable",connection,utilHbase.getNameCFMDO());
            utilHbase.CreateTableHbase("SYSTable",connection,utilHbase.getNameCFSYS());
            long TTLSYS = 3*60*60*1000;
//            long TTLMDO = 7*24*60*60*1000;
            long TTLMDO = 60*1000;
            int portMDO = 11000;
            int portSYS = 11001;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        final TCPSocketServer server1 = new TCPSocketServer(portMDO,TTLMDO,TTLSYS);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }

                }
            }).start();


            new Thread(new Runnable() {
                public void run() {
                    try {
                        final TCPSocketServer server2 = new TCPSocketServer(portSYS,TTLMDO,TTLSYS);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }

                }
            }).start();
        } catch (Exception exp) {
            System.out.println("fail");
            System.out.println("" + exp.getMessage());
        }
    }
}
