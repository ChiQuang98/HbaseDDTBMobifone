package com.mobifone.bigdata.model;

public class ResponseHTTPCount {
    private long number;
    private int statusCode;

    public ResponseHTTPCount() {
    }

    public ResponseHTTPCount(long number, int statusCode) {
        this.number = number;
        this.statusCode = statusCode;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
