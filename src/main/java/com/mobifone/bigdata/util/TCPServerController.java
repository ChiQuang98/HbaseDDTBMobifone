/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobifone.bigdata.util;

import org.apache.hadoop.hbase.client.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class TCPServerController {
    private static volatile AtomicInteger numMessOnSecond = new AtomicInteger(0);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private volatile AtomicBoolean lock = new AtomicBoolean(false);


    public TCPServerController(int port) {
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server TCP with port : " + port + " is running...");

                listening(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void listening(int port) throws IOException {
        Utils utilHbase = Utils.getInstance();
        StreamingUtils streamingUtils = new StreamingUtils();
        Connection connection = utilHbase.GetConnectionHbase();
        long TTLSYS = 3*60*60*1000;
//            long TTLMDO = 7*24*60*60*1000;
        long TTLMDO = 60*1000;
//        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress());
                BufferedReader os = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ObjectInputStream ois;
                ois = new ObjectInputStream(clientSocket.getInputStream());
                new Thread(() -> {
                    if(port==11000){
                        streamingUtils.ProcessingStreamMDO(utilHbase,connection,TTLMDO,ois);
                    } else{
                        streamingUtils.ProcessingStreamSYS(utilHbase,connection,TTLSYS,ois);
                    }
//                    try {
//                        while (true) {
//                            System.out.println("QUANG"+os.readLine());
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    }


}
