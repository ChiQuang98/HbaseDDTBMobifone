package com.mobifone.bigdata.api;

import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class HbaseAPI {
    private static HbaseAPI instance;
    public static HbaseAPI getInstance(){
        if(instance!=null){
            return instance;
        }
        return new HbaseAPI();
    }
    public String CheckPhoneNumberPortPublic(String ipPublic, String phone, int portPublic)  {
        Utils utilHbase = Utils.getInstance();
        JSONObject jsonPortPhone,jsonSubPortPhone;
        try{
            Connection connection = utilHbase.GetConnectionHbase();
            Table tableSYS = connection.getTable(TableName.valueOf("SYSTable"));
            String rowKey = ipPublic;
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes("Info"));
            Result result = tableSYS.get(get);
            if (result.isEmpty()){
                return null;
            }
            else{
                String portPhoneStr = Bytes.toString(result.getValue(Bytes.toBytes("Info"),Bytes.toBytes("PortPhone")));

                if (portPhoneStr==null){
                    return null;
                } else {
                    jsonPortPhone = new JSONObject(portPhoneStr);
                    jsonSubPortPhone = jsonPortPhone.getJSONObject(Integer.toString(portPublic));
//                    System.out.println(jsonSubPortPhone.toString());
                    String phoneNumber = jsonSubPortPhone.getString("PhoneNumber");
                    String timeStamp = jsonSubPortPhone.getString("Timestamp");
                    if (phoneNumber.compareToIgnoreCase(phone)==0){
                        return timeStamp;
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String CheckPhoneNumberWithoutPortPublic(String ipPublic, String phone)  {
        Utils utilHbase = Utils.getInstance();
        JSONObject jsonPortPhone,jsonSubPortPhone;
        try{
            Connection connection = utilHbase.GetConnectionHbase();
            Table tableSYS = connection.getTable(TableName.valueOf("SYSTable"));
            String rowKey = ipPublic;
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes("Info"));
            Result result = tableSYS.get(get);
            if (result.isEmpty()){
                return null;
            }
            else{
                String portPhoneStr = Bytes.toString(result.getValue(Bytes.toBytes("Info"),Bytes.toBytes("PortPhone")));
                System.out.println(portPhoneStr);
                if (portPhoneStr==null){
                    return null;
                } else {
                    jsonPortPhone = new JSONObject(portPhoneStr);
                    Iterator<String> keys = jsonPortPhone.keys();
                    while (keys.hasNext()){
                        String keyPort = keys.next();
                        jsonSubPortPhone = jsonPortPhone.getJSONObject(keyPort);
                        String phoneNumber = jsonSubPortPhone.getString("PhoneNumber");
                        String timeStamp = jsonSubPortPhone.getString("Timestamp");
                        if (phoneNumber.compareToIgnoreCase(phone)==0){
                            return timeStamp;
                        } else {
                            return null;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public void GetRowsByIPSYS(String ipPrivate, int portPrivate, String timeStamp) throws IOException {

//        byte[] rowKeys = Bytes.toBytesBinary("KEY=\\x01\\x01");
//        byte[] fuzzyInfo = {0,0,0,0,1,1};
//        FuzzyRowFilter fuzzyFilter = new FuzzyRowFilter(
//                Arrays.asList(
//                        new Pair<byte[], byte[]>(
//                                rowKeys,
//                                fuzzyInfo)));
//        System.out.println("### fuzzyFilter: " + fuzzyFilter.toString());

        String keyPrefix = ipPrivate+"_"+portPrivate+"_";
        byte[] prefix=Bytes.toBytes(keyPrefix);
        Scan scan = new Scan();
        scan.setRowPrefixFilter(prefix);
        scan.setCaching(5);
        scan.addFamily(Bytes.toBytesBinary("Info"));
        scan.setStartRow(Bytes.toBytesBinary(keyPrefix));
//        scan.setStopRow(Bytes.toBytesBinary("KEY=20"));
//        scan.setFilter(fuzzyFilter);
        Utils utilHbase = new Utils();
        Connection connection = utilHbase.GetConnectionHbase();
        Table table = connection.getTable(TableName.valueOf("SYSTable"));
        ResultScanner results = table.getScanner(scan);
        int count = 0;
        int limit = 10;
        for ( Result r : results ) {
            System.out.println("" + r.toString());
            if (count++ >= limit) break;
        }
    }
}
