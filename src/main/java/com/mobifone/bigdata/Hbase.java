package com.mobifone.bigdata;

import com.mobifone.bigdata.util.StreamingUtils;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.client.*;



import java.io.IOException;

class HBase {
    public static void main(String[] args) throws IOException, Exception {
        try {
            Utils utilHbase = Utils.getInstance();
            StreamingUtils streamingUtils = new StreamingUtils();
            Connection connection = utilHbase.GetConnectionHbase();
            utilHbase.CreateTableHbase("MDOTable",connection,utilHbase.getNameCFMDO());
            utilHbase.CreateTableHbase("SYSTable",connection,utilHbase.getNameCFSYS());
            long TTLSYS = 3*60*60*1000;
//            long TTLMDO = 7*24*60*60*1000;
            long TTLMDO = 60*1000;
            String host = "10.4.200.61";
            int portMDO = 11000;
            int portSYS = 11001;
            //MDO Data Streaming
            new Thread(() -> {
                streamingUtils.ProcessingStreamMDO(utilHbase,connection,TTLMDO,host,portMDO);
            }).start();
            new Thread(() -> {
               streamingUtils.ProcessingStreamSYS(utilHbase,connection,TTLSYS,host,portSYS);

            }).start();
        } catch (Exception exp) {
            System.out.println("fail");
            System.out.println("" + exp.getMessage());
        }
    }
}
