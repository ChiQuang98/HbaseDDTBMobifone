package com.mobifone.bigdata.model;

public class ResponseVTQT {
    String status;
    String data;

    public ResponseVTQT(String status, String data) {
        this.status = status;
        this.data = data;

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
