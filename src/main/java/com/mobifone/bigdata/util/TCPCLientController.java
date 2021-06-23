package com.mobifone.bigdata.util;



import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Ryan
 */
public class TCPCLientController {
    private Socket socket;
    private BufferedReader ois;

    public TCPCLientController(InetAddress IP, int port){
        System.out.println("Clien TCP with"+port+ "is running");
        try{
            socket = new Socket(IP, port);
            System.out.println("Socket: "+IP.getHostAddress()+":"+port);
            getStream();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getStream(){
        try{
            ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String readData() throws IOException, ClassNotFoundException {
        return (String) ois.readLine();
    }


}
