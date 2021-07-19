package com.mobifone.bigdata.model;

public class RequestVTQT {
    private String  msisdn;
    private String  address;
    private String  port;



    public RequestVTQT(String msisdn, String address, String port) {
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
