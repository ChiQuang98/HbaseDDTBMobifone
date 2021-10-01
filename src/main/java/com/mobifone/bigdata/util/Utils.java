package com.mobifone.bigdata.util;


import com.mobifone.bigdata.common.AppConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Properties;


public class Utils {
    private static Utils instance;
    private Connection connection = null;
    private Properties properties;
    public static Utils getInstance() throws IOException {
        if(instance!=null){
            return instance;
        }
        return new Utils();
    }

    public Utils() throws IOException {
        properties = AppConfig.getAppConfigProperties();
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
            "Info",
            "Network",

    };
    private String [][] namecolumSYS = new String[][]{
            {"PortPhone","IPDestPhone"},
            {"IPPrivate","PortPrivate","IPPublic","PortPublic","IPDest","PortDest"},

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
        conf.set("hbase.zookeeper.quorum", properties.getProperty("hbase.host"));
        conf.set("hbase.zookeeper.property.clientPort", properties.getProperty("hbase.port"));
        conf.set("hbase.rootdir","/apps/hbase/data");
//      conf.set("zookeeper.znode.parent","/hbase-secure");
        conf.set("hbase.cluster.distributed","false");
        conf.set("zookeeper.znode.parent","/hbase");
//      conf.set("hbase.defaults.for.version.skip", "true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        conf.set("hbase.client.retries.number", "2");  // default 35
        conf.set("hbase.rpc.timeout", "10000");  // default 60 secs
        conf.set("hbase.rpc.shortoperation.timeout", "10000"); // default 10 secs
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            throw e;
//            return null;
//            e.printStackTrace();
        }
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

}
