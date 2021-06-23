package com.mobifone.bigdata.model;

public class ResponseTimes {
    String status;
    String time;
    String data;

    public ResponseTimes(String status, String data) {
        this.status = status;
        this.data = data;

    }

    public ResponseTimes(String status, String time, String data) {
        this.status = status;
        this.time = time;
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
