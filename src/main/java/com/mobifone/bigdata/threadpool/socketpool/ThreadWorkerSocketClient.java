package com.mobifone.bigdata.threadpool.socketpool;

import com.mobifone.bigdata.util.StreamingUtils;
import com.mobifone.bigdata.util.Utils;
import org.apache.hadoop.hbase.client.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ThreadWorkerSocketClient implements Runnable{
    private Socket socket;
    private int port;
    long TTLMDO;
    long TTLSYS;
    public ThreadWorkerSocketClient() {

    }

    public ThreadWorkerSocketClient(Socket socket, int port, long TTLMDO, long TTLSYS) {
        this.socket = socket;
        this.port = port;
        this.TTLMDO = TTLMDO;
        this.TTLSYS = TTLSYS;
    }

    @Override
    public void run() {

        Utils utilHbase = Utils.getInstance();
        StreamingUtils streamingUtils = StreamingUtils.getInstance();
        Connection connection = null;
        try {
            connection = utilHbase.GetConnectionHbase();
            System.out.println(socket.getInetAddress());
            BufferedReader os = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            if(port==11000){
                streamingUtils.ProcessingStreamMDO(utilHbase,connection,TTLMDO,ois);
            } else{
                streamingUtils.ProcessingStreamSYS(utilHbase,connection,TTLSYS,ois);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
