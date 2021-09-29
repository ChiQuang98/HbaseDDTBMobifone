package com.mobifone.bigdata.model;

public class ResponseHistoryVTQT {
    private long number_of_success;
    private long number_of_fail;
    int status;
    public ResponseHistoryVTQT() {
    }


    public ResponseHistoryVTQT(long number_of_success, long number_of_fail, int status) {
        this.number_of_success = number_of_success;
        this.number_of_fail = number_of_fail;
        this.status = status;
    }

    public long getNumber_of_success() {
        return number_of_success;
    }

    public void setNumber_of_success(long number_of_success) {
        this.number_of_success = number_of_success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getNumber_of_fail() {
        return number_of_fail;
    }

    public void setNumber_of_fail(long number_of_fail) {
        this.number_of_fail = number_of_fail;
    }
}
