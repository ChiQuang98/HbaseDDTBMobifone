package com.mobifone.bigdata.model;

public class Request {
    private String  msisdn;
    private String  address;
    private int  port;



    public Request(String msisdn, String address, int port) {
        this.msisdn = msisdn;
        this.address = address;
        this.port = port;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
