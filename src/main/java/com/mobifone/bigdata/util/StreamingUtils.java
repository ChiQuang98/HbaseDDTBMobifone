package com.mobifone.bigdata.util;


import com.mobifone.bigdata.model.Nat;
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

public class StreamingUtils {
    private static final Logger logger = Logger.getLogger(StreamingUtils.class);
    public void ProcessingStreamMDO(Utils utilHbase, Connection connection, long TTLMDO, ObjectInputStream os) {
        try {

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
                    List<String> arrData = Arrays.asList(data.split("\n"));
                    int len = arrData.size();
                    int pivot1 = len/3;
                    int pivot2 = pivot1*2;
                    List<String>  dataSplit1 = arrData.subList(0,pivot1);
                    List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                    List<String> dataSplit3 = arrData.subList(pivot2,len);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                writeDataMDO(dataSplit1,tableMDO,utilHbase,TTLMDO);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                writeDataMDO(dataSplit2,tableMDO,utilHbase,TTLMDO);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                writeDataMDO(dataSplit3,tableMDO,utilHbase,TTLMDO);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

//
                } catch (Exception e){
                    e.printStackTrace();
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    public void ProcessingStreamSYS(Utils utilHbase, Connection connection, long TTL, ObjectInputStream os) {

        try {
            PrintWriter writer = new PrintWriter("LogInsertTime.txt", "UTF-8");


            UUID uuid = UUID.randomUUID();
            Table tableSYS = connection.getTable(TableName.valueOf("SYSTable"));
            Table tableMDO = connection.getTable(TableName.valueOf("MDOTable"));
            Scan scan = new Scan();

            long countMessageSYS = 0;
            long totalTime = 0;

            while (true) {
                try{
                    String data = (String) os.readObject();
                    List<String> arrData = Arrays.asList(data.split("\n"));
                    int len = arrData.size();
                    int pivot1 = len/3;
                    int pivot2 = pivot1*2;
                    List<String>  dataSplit1 = arrData.subList(0,pivot1);
                    List<String>  dataSplit2 = arrData.subList(pivot1,pivot2);
                    List<String> dataSplit3 = arrData.subList(pivot2,len);
//                    System.out.println("QUANG"+(dataSplit1.size() + dataSplit2.size() + dataSplit3.size()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MappingDataNat(dataSplit1,tableMDO,tableSYS,utilHbase,TTL);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MappingDataNat(dataSplit2,tableMDO,tableSYS,utilHbase,TTL);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MappingDataNat(dataSplit3,tableMDO,tableSYS,utilHbase,TTL);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (Exception e){
                    e.printStackTrace();
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MappingDataNat(List<String> dataList,Table tableMDO, Table tableSYS, Utils utilHbase, long TTL) throws ParseException, IOException {
//        String data = "ss";
        long start = System.currentTimeMillis();
        JSONObject jsonPortPhone,jsonIPDestPhone,jsonSubPortPhone;
        int countRowMatch = 0;
        start = System.currentTimeMillis();
//        System.out.println(data);
        int len = dataList.size();
        for(int i = 0;i<len;i++ ){
            jsonPortPhone = new JSONObject();
            jsonIPDestPhone = new JSONObject();
            jsonSubPortPhone = new JSONObject();
            String data = dataList.get(i);
            long finish = System.currentTimeMillis();
//        totalTime += finish - start;
//        countMessageSYS++;
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
                        e.printStackTrace();
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
        System.out.println("QUANG COUNT: "+countRowMatch);
    }
    private void writeDataMDO(List<String> dataList,Table tableMDO,Utils utilHbase,long TTLMDO) throws ParseException, IOException {
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
                e.printStackTrace();
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


