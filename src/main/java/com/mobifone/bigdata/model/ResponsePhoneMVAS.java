package com.mobifone.bigdata.model;

public class ResponsePhoneMVAS {
    String status,MSISDN;



    public ResponsePhoneMVAS(String status, String MSISDN) {
        this.status = status;
        this.MSISDN = MSISDN;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }
}
