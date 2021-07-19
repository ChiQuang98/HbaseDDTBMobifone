package com.mobifone.bigdata.model;

public class RequestMVAS {
    String sub_ip_address;
    String sub_ip_port;
    String destination_address;

    public RequestMVAS() {
    }

    public RequestMVAS(String sub_ip_address, String sub_ip_port, String destination_address) {
        this.sub_ip_address = sub_ip_address;
        this.sub_ip_port = sub_ip_port;
        this.destination_address = destination_address;
    }

    public String getSub_ip_address() {
        return sub_ip_address;
    }

    public void setSub_ip_address(String sub_ip_address) {
        this.sub_ip_address = sub_ip_address;
    }

    public String getSub_ip_port() {
        return sub_ip_port;
    }

    public void setSub_ip_port(String sub_ip_port) {
        this.sub_ip_port = sub_ip_port;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }
}
