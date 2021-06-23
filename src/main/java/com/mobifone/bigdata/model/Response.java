package com.mobifone.bigdata.model;

public class Response {
    String status;
    String data;

    public Response(String status, String data) {
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
