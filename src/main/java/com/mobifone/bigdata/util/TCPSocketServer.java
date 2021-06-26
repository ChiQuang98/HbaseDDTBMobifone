/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobifone.bigdata.util;

import com.mobifone.bigdata.threadpool.socketpool.FixedThreadPoolSocket;
import com.mobifone.bigdata.threadpool.socketpool.ThreadWorkerSocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class TCPSocketServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
//    long TTLMDO,TTLSYS;
    public TCPSocketServer(int port,long TTLMDO,long TTLSYS) {
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server TCP with port : " + port + " is running...");
                listening(port,TTLMDO,TTLSYS);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    private void listening(int port,long TTLMDO,long TTLSYS) throws IOException {
        FixedThreadPoolSocket threadpool = FixedThreadPoolSocket.getInstance(5);
        ExecutorService executorService = threadpool.getExecutorService();
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                ThreadWorkerSocketClient threadClientSocket = new ThreadWorkerSocketClient(clientSocket,port,TTLMDO,TTLSYS);
                executorService.execute(threadClientSocket);
            } catch (Exception e) {
                clientSocket.close();
                //e.printStackTrace();
            }
        }
    }


}
