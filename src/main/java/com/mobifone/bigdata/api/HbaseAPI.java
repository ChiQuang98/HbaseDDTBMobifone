package com.mobifone.bigdata.api;

import com.mobifone.bigdata.HbaseController;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Iterator;

public class HbaseAPI {
    private static HbaseAPI instance;
    private static final Logger logger = Logger.getLogger(HbaseAPI.class);
    public static HbaseAPI getInstance(){
        if(instance!=null){
            return instance;
        }
        return new HbaseAPI();
    }



    public String CheckPortPublicWithIpDest(String ipPublic, int portPublic, String ipDest) throws IOException {
        Utils utilHbase = Utils.getInstance();
        JSONObject jsonIPDestPhone,jsonSubIPDestPhone;
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
            String ipDestPhoneStr = Bytes.toString(result.getValue(Bytes.toBytes("Info"),Bytes.toBytes("IPDestPhone")));
            if (ipDestPhoneStr==null){
                return null;
            } else {
                jsonIPDestPhone = new JSONObject(ipDestPhoneStr);
                jsonSubIPDestPhone = jsonIPDestPhone.getJSONObject(ipDest);
//                    System.out.println(jsonSubPortPhone.toString());
                String phoneNumber = jsonSubIPDestPhone.getString("PhoneNumber");
//                String timeStamp = jsonSubPortPhone.getString("Timestamp");
                String portPublicGet = jsonSubIPDestPhone.getString("PortPublic");
                int portPublicInterger = Integer.parseInt(portPublicGet);
                if (portPublicInterger==portPublic){
                    return phoneNumber;
                } else {
                    return null;
                }
            }
        }
//        } catch (Exception e) {
//            //e.printStackTrace();
//            return null;
//        }
    }
    public String CheckPortPublicWithoutIpDest(String ipPublic, int portPublic) throws  IOException {
        Utils utilHbase = Utils.getInstance();
        JSONObject jsonIPDestPhone,jsonSubIPDestPhone;
//        try{
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
            String ipDestPhoneStr = Bytes.toString(result.getValue(Bytes.toBytes("Info"),Bytes.toBytes("IPDestPhone")));
            if (ipDestPhoneStr==null){
                return null;
            } else {
                jsonIPDestPhone = new JSONObject(ipDestPhoneStr);
                Iterator<String> keys = jsonIPDestPhone.keys();
                while (keys.hasNext()){
                    String keyPort = keys.next();
                    jsonSubIPDestPhone = jsonIPDestPhone.getJSONObject(keyPort);
                    String phoneNumber = jsonSubIPDestPhone.getString("PhoneNumber");
//                    String timeStamp = jsonSubPortPhone.getString("Timestamp");
                    int portPublicGet = Integer.parseInt(jsonSubIPDestPhone.getString("PortPublic"));
                    if (portPublicGet==portPublic){
                        return phoneNumber;
                    }
                }
                return null;
            }
        }
    }
    public String CheckPhoneNumberPortPublic(String ipPublic, String phone, int portPublic) throws IOException {
        Utils utilHbase = null;
        try{
            utilHbase = Utils.getInstance();
        } catch (Exception e){
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        if (utilHbase == null){
            logger.error("Null get intance connect Hbase");
            System.out.println("UTILHBASE NULL");
        }
        JSONObject jsonPortPhone,jsonSubPortPhone;
//        try{
            Connection connection = utilHbase.GetConnectionHbase();
            if (connection == null){
                throw new ConnectException();
            }
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
                    try{
                        jsonSubPortPhone = jsonPortPhone.getJSONObject(Integer.toString(portPublic));
//                    System.out.println(jsonSubPortPhone.toString());
                        String phoneNumber = jsonSubPortPhone.getString("PhoneNumber");
                        String timeStamp = jsonSubPortPhone.getString("Timestamp");
                        if (phoneNumber.compareToIgnoreCase(phone)==0){
                            return timeStamp;
                        } else {
                            return null;
                        }
                    } catch (Exception e){
                        logger.error(e.getMessage());
                        return null;
                    }

                }
            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//            return null;
//        }
    }
    public String CheckPhoneNumberWithoutPortPublic(String ipPublic, String phone) throws  IOException {
        Utils utilHbase = Utils.getInstance();
        JSONObject jsonPortPhone,jsonSubPortPhone;
        Connection connection = utilHbase.GetConnectionHbase();
        if (connection == null){
            throw new ConnectException();
        }
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
                    try{
                        jsonSubPortPhone = jsonPortPhone.getJSONObject(keyPort);
                        String phoneNumber = jsonSubPortPhone.getString("PhoneNumber");
                        String timeStamp = jsonSubPortPhone.getString("Timestamp");
                        if (phoneNumber.compareToIgnoreCase(phone)==0){
                            return timeStamp;
                        }
                    } catch (Exception e){
                        logger.error(e.getMessage());
                    }
                }
                return null;
            }
        }
    }

}
