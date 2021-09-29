package com.mobifone.bigdata.model;

public class RequestHistoryVTQT {
    private String timestart;
    private String timeend;

    public RequestHistoryVTQT() {
    }

    public RequestHistoryVTQT(String time_start, String time_end) {
        this.timestart = time_start;
        this.timeend = time_end;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }
}
