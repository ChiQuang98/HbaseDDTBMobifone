package com.mobifone.bigdata.util;


import com.mobifone.bigdata.model.Nat;
import com.mobifone.bigdata.threadpool.mdothreadpool.CachedThreadPoolMDO;
import com.mobifone.bigdata.threadpool.mdothreadpool.ThreadWorkerMDO;
import com.mobifone.bigdata.threadpool.natsthreadpool.CachedThreadPoolNats;
import com.mobifone.bigdata.threadpool.natsthreadpool.ThreadWorkerNats;
import com.mobifone.bigdata.threadpool.socketpool.FixedThreadPoolSocket;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

public class StreamingUtils {
    private int levelLine2 = 2000;
    private int levelLine4 = 4000;
    private int levelLine6 = 6000;
    private int levelLine8 = 8000;
    private int levelLine10 = 10000;
//    private  volatile AtomicLong counterTimeNats = new AtomicLong(0);
    private static final Logger logger = Logger.getLogger(StreamingUtils.class);
    private static StreamingUtils instance;
    public static StreamingUtils getInstance(){
        if(instance==null){
            instance = new StreamingUtils();
        }
        return instance;
    }
    public void ProcessingStreamMDO(Utils utilHbase, Connection connection, long TTLMDO, ObjectInputStream os) {
        try {
            CachedThreadPoolMDO threadpoolMDO = CachedThreadPoolMDO.getInstance();
            ExecutorService executorService = threadpoolMDO.getExecutorService();
            long index = 0;
            UUID uuid = UUID.randomUUID();
            Table tableMDO = connection.getTable(TableName.valueOf("MDOTable"));
            long start = System.currentTimeMillis();
            int messageMDOCOUNT = 0;
            long totalTime = 0;
            while (true) {
                start = System.currentTimeMillis();
                try{
                    String data = (String) os.readObject();
//                    System.out.println(data);
                    List<String> arrData = Arrays.asList(data.split("\n"));
                    int len = arrData.size();
                    if(len<=levelLine2){
                        ThreadWorkerMDO threadWorkerMDO = new ThreadWorkerMDO(TTLMDO,tableMDO,arrData);
                        executorService.execute(threadWorkerMDO);
                    } else if(len <= levelLine4){
                        int pivot = len/2;
                        List<String>  dataSplit1 = arrData.subList(0,pivot);
                        List<String>  dataSplit2 = arrData.subList(pivot,len);
                        ThreadWorkerMDO threadWorkerMDO1 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit1);
                        ThreadWorkerMDO threadWorkerMDO2 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit2);
                        executorService.execute(threadWorkerMDO1);
                        executorService.execute(threadWorkerMDO2);
                    } else if(len<=levelLine6){
                        int pivot1 = len/4;
                        int pivot2 = pivot1 * 2;
                        int pivot3 = pivot1*3;
                        List<String>  dataSplit1 = arrData.subList(0,pivot1);
                        List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                        List<String> dataSplit3 = arrData.subList(pivot2,pivot3);
                        List<String> dataSplit4 = arrData.subList(pivot3,len);
                        ThreadWorkerMDO threadWorkerMDO1 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit1);
                        ThreadWorkerMDO threadWorkerMDO2 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit2);
                        ThreadWorkerMDO threadWorkerMDO3 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit3);
                        ThreadWorkerMDO threadWorkerMDO4 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit4);
                        executorService.execute(threadWorkerMDO1);
                        executorService.execute(threadWorkerMDO2);
                        executorService.execute(threadWorkerMDO3);
                        executorService.execute(threadWorkerMDO4);
                    } else if( len<= levelLine8){
                        int pivot1 = len/5;
                        int pivot2 = pivot1 * 2;
                        int pivot3 = pivot1*3;
                        int pivot4 = pivot1*4;
                        List<String>  dataSplit1 = arrData.subList(0,pivot1);
                        List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                        List<String> dataSplit3 = arrData.subList(pivot2,pivot3);
                        List<String> dataSplit4 = arrData.subList(pivot3,pivot4);
                        List<String> dataSplit5 = arrData.subList(pivot3,len);
                        ThreadWorkerMDO threadWorkerMDO1 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit1);
                        ThreadWorkerMDO threadWorkerMDO2 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit2);
                        ThreadWorkerMDO threadWorkerMDO3 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit3);
                        ThreadWorkerMDO threadWorkerMDO4 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit4);
                        ThreadWorkerMDO threadWorkerMDO5 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit5);
                        executorService.execute(threadWorkerMDO1);
                        executorService.execute(threadWorkerMDO2);
                        executorService.execute(threadWorkerMDO3);
                        executorService.execute(threadWorkerMDO4);
                        executorService.execute(threadWorkerMDO5);
                    } else {
                        int pivot1 = len/6;
                        int pivot2 = pivot1 * 2;
                        int pivot3 = pivot1*3;
                        int pivot4 = pivot1*4;
                        int pivot5 = pivot1*5;
                        List<String>  dataSplit1 = arrData.subList(0,pivot1);
                        List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                        List<String> dataSplit3 = arrData.subList(pivot2,pivot3);
                        List<String> dataSplit4 = arrData.subList(pivot3,pivot4);
                        List<String> dataSplit5 = arrData.subList(pivot4,pivot5);
                        List<String> dataSplit6 = arrData.subList(pivot5,len);
                        ThreadWorkerMDO threadWorkerMDO1 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit1);
                        ThreadWorkerMDO threadWorkerMDO2 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit2);
                        ThreadWorkerMDO threadWorkerMDO3 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit3);
                        ThreadWorkerMDO threadWorkerMDO4 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit4);
                        ThreadWorkerMDO threadWorkerMDO5 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit5);
                        ThreadWorkerMDO threadWorkerMDO6 = new ThreadWorkerMDO(TTLMDO,tableMDO,dataSplit6);
                        executorService.execute(threadWorkerMDO1);
                        executorService.execute(threadWorkerMDO2);
                        executorService.execute(threadWorkerMDO3);
                        executorService.execute(threadWorkerMDO4);
                        executorService.execute(threadWorkerMDO5);
                        executorService.execute(threadWorkerMDO6);
                    }
                } catch (Exception e){
                    //e.printStackTrace();
                    break;
                }

            }
        } catch (Exception e) {
            //e.printStackTrace();
//            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    public void ProcessingStreamSYS(Utils utilHbase, Connection connection, long TTL, ObjectInputStream os) {
        try {
            UUID uuid = UUID.randomUUID();
            Table tableSYS = connection.getTable(TableName.valueOf("SYSTable"));
            Table tableMDO = connection.getTable(TableName.valueOf("MDOTable"));
            Scan scan = new Scan();

            long countMessageSYS = 0;
            long totalTime = 0;
            CachedThreadPoolNats threadpoolNats = CachedThreadPoolNats.getInstance();
            ExecutorService executorService = threadpoolNats.getExecutorService();
            while (true) {
                try{
                    String data = (String) os.readObject();
                    List<String> arrData = Arrays.asList(data.split("\n"));
                    int len = arrData.size();
                    if(len<=levelLine2){
                        int pivot = len/2;
                        List<String>  dataSplit1 = arrData.subList(0,pivot);
                        List<String>  dataSplit2 = arrData.subList(pivot,len);
                        ThreadWorkerNats threadWorkerNats1 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit1);
                        ThreadWorkerNats threadWorkerNats2 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit2);
                        executorService.execute(threadWorkerNats1);
                        executorService.execute(threadWorkerNats2);
                    } else if(len <= levelLine4){
                        int pivot = len/3;
                        int pivot1 = pivot * 2;
                        List<String>  dataSplit1 = arrData.subList(0,pivot);
                        List<String>  dataSplit2 = arrData.subList(pivot,pivot1);
                        List<String>  dataSplit3 = arrData.subList(pivot1,len);
                        ThreadWorkerNats threadWorkerNats1 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit1);
                        ThreadWorkerNats threadWorkerNats2 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit2);
                        ThreadWorkerNats threadWorkerNats3 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit3);
                        executorService.execute(threadWorkerNats1);
                        executorService.execute(threadWorkerNats2);
                        executorService.execute(threadWorkerNats3);
                    } else if(len<=levelLine6){
                        int pivot1 = len/5;
                        int pivot2 = pivot1 * 2;
                        int pivot3 = pivot1*3;
                        int pivot4 = pivot1*4;
                        List<String>  dataSplit1 = arrData.subList(0,pivot1);
                        List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                        List<String> dataSplit3 = arrData.subList(pivot2,pivot3);
                        List<String> dataSplit4 = arrData.subList(pivot3,pivot4);
                        List<String> dataSplit5 = arrData.subList(pivot4,len);
                        ThreadWorkerNats threadWorkerNats1 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit1);
                        ThreadWorkerNats threadWorkerNats2 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit2);
                        ThreadWorkerNats threadWorkerNats3 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit3);
                        ThreadWorkerNats threadWorkerNats4 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit4);
                        ThreadWorkerNats threadWorkerNats5 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit5);
                        executorService.execute(threadWorkerNats1);
                        executorService.execute(threadWorkerNats2);
                        executorService.execute(threadWorkerNats3);
                        executorService.execute(threadWorkerNats4);
                        executorService.execute(threadWorkerNats5);
                    } else if( len<= levelLine8){
                        int pivot1 = len/6;
                        int pivot2 = pivot1 * 2;
                        int pivot3 = pivot1*3;
                        int pivot4 = pivot1*4;
                        int pivot5 = pivot1*5;
                        List<String>  dataSplit1 = arrData.subList(0,pivot1);
                        List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                        List<String> dataSplit3 = arrData.subList(pivot2,pivot3);
                        List<String> dataSplit4 = arrData.subList(pivot3,pivot4);
                        List<String> dataSplit5 = arrData.subList(pivot4,pivot5);
                        List<String> dataSplit6 = arrData.subList(pivot5,len);
                        ThreadWorkerNats threadWorkerNats1 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit1);
                        ThreadWorkerNats threadWorkerNats2 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit2);
                        ThreadWorkerNats threadWorkerNats3 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit3);
                        ThreadWorkerNats threadWorkerNats4 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit4);
                        ThreadWorkerNats threadWorkerNats5 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit5);
                        ThreadWorkerNats threadWorkerNats6 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit6);
                        executorService.execute(threadWorkerNats1);
                        executorService.execute(threadWorkerNats2);
                        executorService.execute(threadWorkerNats3);
                        executorService.execute(threadWorkerNats4);
                        executorService.execute(threadWorkerNats5);
                        executorService.execute(threadWorkerNats6);
                    } else {
                        int pivot1 = len/7;
                        int pivot2 = pivot1 * 2;
                        int pivot3 = pivot1*3;
                        int pivot4 = pivot1*4;
                        int pivot5 = pivot1*5;
                        int pivot6 = pivot1*6;
                        List<String>  dataSplit1 = arrData.subList(0,pivot1);
                        List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                        List<String> dataSplit3 = arrData.subList(pivot2,pivot3);
                        List<String> dataSplit4 = arrData.subList(pivot3,pivot4);
                        List<String> dataSplit5 = arrData.subList(pivot4,pivot5);
                        List<String> dataSplit6 = arrData.subList(pivot5,pivot6);
                        List<String> dataSplit7 = arrData.subList(pivot6,len);
                        ThreadWorkerNats threadWorkerNats1 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit1);
                        ThreadWorkerNats threadWorkerNats2 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit2);
                        ThreadWorkerNats threadWorkerNats3 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit3);
                        ThreadWorkerNats threadWorkerNats4 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit4);
                        ThreadWorkerNats threadWorkerNats5 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit5);
                        ThreadWorkerNats threadWorkerNats6 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit6);
                        ThreadWorkerNats threadWorkerNats7 = new ThreadWorkerNats(TTL,tableSYS,tableMDO,dataSplit7);
                        executorService.execute(threadWorkerNats1);
                        executorService.execute(threadWorkerNats2);
                        executorService.execute(threadWorkerNats3);
                        executorService.execute(threadWorkerNats4);
                        executorService.execute(threadWorkerNats5);
                        executorService.execute(threadWorkerNats6);
                        executorService.execute(threadWorkerNats7);
                    }
                    long end = System.currentTimeMillis();
//                    System.out.println("TIME========: "+counterTimeNats.get());
                } catch (Exception e){
                    //e.printStackTrace();
                    break;
                }

            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void MappingDataNat(List<String> dataList,Table tableMDO, Table tableSYS, Utils utilHbase, long TTL) throws ParseException, IOException {
        long start = System.currentTimeMillis();
        JSONObject jsonPortPhone,jsonIPDestPhone,jsonSubPortPhone;
        int countRowMatch = 0;
        int len = dataList.size();
        for(int i = 0;i<len;i++ ){
            jsonPortPhone = new JSONObject();
            jsonIPDestPhone = new JSONObject();
            jsonSubPortPhone = new JSONObject();
            String data = dataList.get(i);
            long finish = System.currentTimeMillis();
            String[] rowData = data.split(",");
            String portPublic = rowData[5];
            Nat natObj = new Nat(rowData[2],rowData[3],rowData[4],rowData[5],rowData[6],rowData[7]);
            natObj.setTimeStamp(rowData[1]);
            Date dateRowSYS, dateMDOCol1, dateMDOCol2;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            dateRowSYS = df.parse(natObj.getTimeStamp());
            Get get = new Get(Bytes.toBytes("KEY|" + natObj.getiPPrivate()));
            get.addFamily(Bytes.toBytes("Info"));
            get.addFamily(Bytes.toBytes("Times"));
            get.addFamily(Bytes.toBytes("Type"));
            Result result = tableMDO.get(get);
            if (!result.isEmpty()) {
                String timeStampMDOCol1 = Bytes.toString(result.getValue(Bytes.toBytes("Times"), Bytes.toBytes("TimestampCol1")));
                String timeStampMDOCol2 = Bytes.toString(result.getValue(Bytes.toBytes("Times"), Bytes.toBytes("TimestampCol2")));
                String typeBegin = Bytes.toString(result.getValue(Bytes.toBytes("Type"), Bytes.toBytes("TypeBegin")));
                String phoneMDOCol1 = Bytes.toString(result.getValue(Bytes.toBytes("Info"), Bytes.toBytes("PhoneNumberCol1")));
                String phoneMDOCol2 = Bytes.toString(result.getValue(Bytes.toBytes("Info"), Bytes.toBytes("PhoneNumberCol2")));
                Get getSYS = new Get(Bytes.toBytes(natObj.getiPPublic()));
                getSYS.addFamily(Bytes.toBytes("Info"));
                Result resultSYS = tableSYS.get(getSYS);
                if(!resultSYS.isEmpty()){
                    String portPhoneStr = Bytes.toString(resultSYS.getValue(Bytes.toBytes("Info"), Bytes.toBytes("PortPhone")));
                    String ipDestPhoneStr = Bytes.toString(resultSYS.getValue(Bytes.toBytes("Info"), Bytes.toBytes("IPDestPhone")));
                    jsonPortPhone = new JSONObject(portPhoneStr);
                    try{
                        jsonSubPortPhone =  jsonPortPhone.getJSONObject(natObj.getPortPublic());
                        jsonSubPortPhone = new JSONObject();
                    } catch (Exception e){
                        //e.printStackTrace();
                    }
                    jsonIPDestPhone = new JSONObject(ipDestPhoneStr);
//                        jsonPortPhone.put("")
                }
                if (typeBegin != null && timeStampMDOCol1 != null && phoneMDOCol1 != null && timeStampMDOCol2 != null && phoneMDOCol2 != null) {
                    if (typeBegin.compareToIgnoreCase("Start") == 0) {
                        dateMDOCol1 = df.parse(timeStampMDOCol1);
                        dateMDOCol2 = df.parse(timeStampMDOCol2);
                        String rowKey = natObj.getiPPublic();
//                                System.out.println(rowKey);

                        if (dateRowSYS.getTime() >= dateMDOCol2.getTime()) {
                            natObj.setPhoneNumber(phoneMDOCol2);
                            jsonSubPortPhone.put("Timestamp",natObj.getTimeStamp());
                            jsonSubPortPhone.put("PhoneNumber",natObj.getPhoneNumber());
                            jsonPortPhone.put(natObj.getPortPublic(),jsonSubPortPhone);
                            jsonIPDestPhone.put(natObj.getIpDest(),phoneMDOCol2);
//                                System.out.println(jsonPortPhone.toString());
                            natObj.setJsonPortPhone(jsonPortPhone.toString());
                            natObj.setJsonIPDestPhone(jsonIPDestPhone.toString());
                            //Key Pattern: IPPUBLIC_PortPublic
                            boolean isDone = utilHbase.insertData(tableSYS, rowKey, utilHbase.getNameCFSYS(), utilHbase.getNamecolumSYS(), TTL, natObj);
                            if (isDone) {
                                countRowMatch++;
                                finish = System.currentTimeMillis();
//                                    System.out.println("TIME Before - After: " + start + " | " + finish);
                                long timeElapsed = finish - start;
                                String log = "Inserted PhoneNumber:"+phoneMDOCol2+" With (IpPublic: "+rowData[4]+"| PortPublic: "+rowData[5]+") to Table SYS: "  + " In: " + timeElapsed;
//                               System.out.println(log);
//                                        logger.info(log);
//                                        writer.println(log);
                            }
                        } else if (dateRowSYS.getTime() >= dateMDOCol1.getTime()) {
                            natObj.setPhoneNumber(phoneMDOCol1);
                            jsonSubPortPhone.put("Timestamp",natObj.getTimeStamp());
                            jsonSubPortPhone.put("PhoneNumber",natObj.getPhoneNumber());
                            jsonPortPhone.put(natObj.getPortPublic(),jsonSubPortPhone);
                            jsonIPDestPhone.put(natObj.getIpDest(),phoneMDOCol1);
//                                System.out.println(jsonPortPhone.toString());
                            natObj.setJsonPortPhone(jsonPortPhone.toString());
                            natObj.setJsonIPDestPhone(jsonIPDestPhone.toString());
                            //Key Pattern: IPPUBLIC_PortPublic
                            boolean isDone = utilHbase.insertData(tableSYS, rowKey, utilHbase.getNameCFSYS(), utilHbase.getNamecolumSYS(), TTL, natObj);
                            if (isDone) {
                                countRowMatch++;
                                finish = System.currentTimeMillis();
//                                    System.out.println("TIME Before - After: " + start + " | " + finish);
                                String log = "Inserted PhoneNumber:"+phoneMDOCol1+" With (IpPublic: "+rowData[4]+"| PortPublic: "+rowData[5]+") to Table SYS: "  + " In: ";
//                                System.out.println(log);
//                                        logger.info(log);
//                                        writer.println(log);
                            }
                        }
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        long total = end-start;
        System.out.println("TIMEQUANG: "+total);

    }
    public void writeDataMDO(List<String> dataList,Table tableMDO,Utils utilHbase,long TTLMDO) throws ParseException, IOException {
        String[] col2 = {"", ""};
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date dateCol1, dateCol2, dateCurr;
        long finish = System.currentTimeMillis();
        int len = dataList.size();
        for(int i=0;i<len;i++){
            String data = dataList.get(i);
            String[] rowData = data.split(",");
            try{
                dateCurr = df.parse(rowData[0]);
            } catch (Exception e){
                System.out.println(data);
                System.out.println("DATE" + rowData[0]);
                //e.printStackTrace();
                continue;
            }

            if (rowData.length < 5) {
                continue;
            }
            String rowName = "KEY|" + rowData[4];
//                System.out.println(rowName);
//                System.out.println(rowName);
            Get get = new Get(Bytes.toBytes(rowName));
            get.addFamily(Bytes.toBytes("Info"));
            get.addFamily(Bytes.toBytes("Times"));
            get.addFamily(Bytes.toBytes("Type"));
            Result result = tableMDO.get(get);
            if (result.isEmpty()) {
                utilHbase.insertDataMDO(tableMDO, rowName, utilHbase.getNameCFMDO(), utilHbase.getNamecolumMDO(), TTLMDO, Utils.typeMDONull, col2, rowData);
            } else {
                String timeStampCol1Str = Bytes.toString(result.getValue(Bytes.toBytes("Times"), Bytes.toBytes("TimestampCol1")));
                String timeStampCol2Str = Bytes.toString(result.getValue(Bytes.toBytes("Times"), Bytes.toBytes("TimestampCol2")));
//                    String phoneCol1Str = Bytes.toString(result.getValue(Bytes.toBytes("Info"),Bytes.toBytes("PhoneNumberCol1")));
                String phoneCol2Str = Bytes.toString(result.getValue(Bytes.toBytes("Info"), Bytes.toBytes("PhoneNumberCol2")));
                dateCol1 = df.parse(timeStampCol1Str);
                dateCol2 = df.parse(timeStampCol2Str);
                if (dateCol2.getTime() <= dateCurr.getTime()) {
                    //bo col1, col2 ve col1, curr thanh col2
                    col2[0] = timeStampCol2Str;
                    col2[1] = phoneCol2Str;
                    utilHbase.insertDataMDO(tableMDO, rowName, utilHbase.getNameCFMDO(), utilHbase.getNamecolumMDO(), TTLMDO, Utils.typeMDOExistCol2Curr, col2, rowData);
                } else if (dateCol1.getTime() <= dateCurr.getTime()) {
                    //bo col1, curr thanh col1, col2 giu nguyen
                    System.out.println("CHECK: " + rowName);
                    utilHbase.insertDataMDO(tableMDO, rowName, utilHbase.getNameCFMDO(), utilHbase.getNamecolumMDO(), TTLMDO, Utils.typeMDOExistCol1Curr, col2, rowData);
                }
            }
        }

    }
}


