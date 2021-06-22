package com.mobifone.bigdata.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


public class Utils {
    private static Utils instance;

    public static Utils getInstance() {
        if(instance!=null){
            return instance;
        }
        return new Utils();
    }

    public static final int typeMDONull = 0;
    public static final int typeMDOExistCol2Curr = 1;
    public static final int typeMDOExistCol1Curr = 2;
    private String[] nameCFMDO = new String[]{
            "Times",
            "Content",
            "Type",
            "Info",
            "Network"
    };
    private String [][] namecolumMDO = new String[][]{
            {"TimestampCol1","TimestampCol2"},
            {"MessageMDO"},
            {"TypeBegin"},
            {"PhoneNumberCol1","PhoneNumberCol2"},
            {"IPPrivate"},
    };
    private String[] nameCFSYS = new String[]{
            "Site",
            "Times",
            "Network",
            "Info"
    };
    private String [][] namecolumSYS = new String[][]{
            {"SiteName"},
            {"Timestamp"},
            {"IPPrivate","PortPrivate","IPPublic","PortPublic","IPDest","PortDest"},
            {"PhoneNumber"}
    };
    public String[] getNameCFMDO() {
        return nameCFMDO;
    }

    public String[][] getNamecolumMDO() {
        return namecolumMDO;
    }

    public String[] getNameCFSYS() {
        return nameCFSYS;
    }

    public String[][] getNamecolumSYS() {
        return namecolumSYS;
    }

    public Connection GetConnectionHbase() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.clear();
        conf.set("hbase.zookeeper.quorum", "localhost");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.rootdir","/apps/hbase/data");
//      conf.set("zookeeper.znode.parent","/hbase-secure");
        conf.set("hbase.cluster.distributed","false");
        conf.set("zookeeper.znode.parent","/hbase");
//      conf.set("hbase.defaults.for.version.skip", "true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        conf.set("hbase.client.retries.number", "2");  // default 35
        conf.set("hbase.rpc.timeout", "10000");  // default 60 secs
        conf.set("hbase.rpc.shortoperation.timeout", "10000"); // default 10 secs
        Connection connection = ConnectionFactory.createConnection(conf);
        if (connection!=null){
            return connection;
        }
        return null;
    }
    public boolean CreateTableHbase(String tableName, Connection connection, String... columFamily) throws IOException {
//        Connection connection;
        Admin admin = connection.getAdmin();
        int numCF = columFamily.length;
        if (!admin.tableExists(TableName.valueOf(tableName))){
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for (String CfName : columFamily){
                tableDescriptor.addFamily(new HColumnDescriptor(CfName));
            }
            admin.createTable(tableDescriptor);
            System.out.println("Table Created: "+tableName);
            return true;
        }
        System.out.println("Fail or Table Existed: "+tableName);
        return false;
    }
    public boolean insertData(Table table,String keyRow,  String []columFamily,String[][] colums,long TTL, String... value){
        Put p = new Put(Bytes.toBytes(keyRow));
        int lenColumFamily = columFamily.length;
        int index = 0;
        for(int i=0;i<lenColumFamily;i++){
            int lenColumeEachFamily = colums[i].length;
            for(int j=0;j<lenColumeEachFamily;j++){
                p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                index++;
            }
        }
        try {
            table.put(p);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean insertDataMDO(Table table, String keyRow, String[] columFamily, String[][] colums, long TTL, int typeInsert,String []col2, String []value){
        Put p = new Put(Bytes.toBytes(keyRow));
        int lenColumFamily = columFamily.length;
        int index = 0;
        switch (typeInsert){
            case typeMDONull:
                for(int i=0;i<lenColumFamily;i++){
                    int lenColumEachFamily = colums[i].length;
                    for(int j=0;j<lenColumEachFamily;j++){
                        if (i==0||i==3){
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            if(j==1){
                                index++;
                            }
                        } else{
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        }
                    }
                }
                try {
                    table.put(p);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            case typeMDOExistCol1Curr:
                for(int i=0;i<lenColumFamily;i++){
                    int lenColumEachFamily = colums[i].length;
                    for(int j=0;j<lenColumEachFamily;j++){
                        if (i==0&&j==0){//col1
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        } else if (i ==0 && j==1){//col2 giu nguyen
                        } else if (i == 3 && j==0){
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        } else if(i==3 && j==1){
                        } else{
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        }
                    }
                }
                try {
                    table.put(p);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            case typeMDOExistCol2Curr:
                for(int i=0;i<lenColumFamily;i++){
                    int lenColumEachFamily = colums[i].length;
                    for(int j=0;j<lenColumEachFamily;j++){
                        if (i==0&&j==0){
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(col2[0])).setTTL(TTL);
                        } else if (i ==0 && j==1){
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        } else if (i == 3 && j==0){
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(col2[1])).setTTL(TTL);
                        } else if(i==3 && j==1){
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        } else{
                            p.addColumn(Bytes.toBytes(columFamily[i]), Bytes.toBytes(colums[i][j]), Bytes.toBytes(value[index])).setTTL(TTL);
                            index++;
                        }
                    }
                }
                try {
                    table.put(p);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
        }
        return false;
    }
}
