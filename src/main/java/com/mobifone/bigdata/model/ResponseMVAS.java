package com.mobifone.bigdata.model;

public class ResponseMVAS {
    String status;

    public ResponseMVAS() {
    }

    public ResponseMVAS(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
