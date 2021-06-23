package com.mobifone.bigdata.model;

public class Nat {
    private String timeStamp,iPPrivate,portPrivate,iPPublic,portPublic,ipDest,portDest,phoneNumber,jsonPortPhone,jsonIPDestPhone;

    public Nat() {
    }

    public String getJsonIPDestPhone() {
        return jsonIPDestPhone;
    }

    public void setJsonIPDestPhone(String jsonIPDestPhone) {
        this.jsonIPDestPhone = jsonIPDestPhone;
    }

    public String getJsonPortPhone() {
        return jsonPortPhone;
    }

    public void setJsonPortPhone(String jsonPortPhone) {
        this.jsonPortPhone = jsonPortPhone;
    }

    public Nat(String iPPrivate, String portPrivate, String iPPublic, String portPublic, String ipDest, String portDest) {
        this.iPPrivate = iPPrivate;
        this.portPrivate = portPrivate;
        this.iPPublic = iPPublic;
        this.portPublic = portPublic;
        this.ipDest = ipDest;
        this.portDest = portDest;
    }

    public Nat(String timeStamp, String iPPrivate, String portPrivate, String iPPublic, String portPublic, String ipDest, String portDest) {
        this.timeStamp = timeStamp;
        this.iPPrivate = iPPrivate;
        this.portPrivate = portPrivate;
        this.iPPublic = iPPublic;
        this.portPublic = portPublic;
        this.ipDest = ipDest;
        this.portDest = portDest;
    }

    public Nat(String timeStamp, String iPPrivate, String portPrivate, String iPPublic, String portPublic, String ipDest, String portDest, String phoneNumber) {
        this.timeStamp = timeStamp;
        this.iPPrivate = iPPrivate;
        this.portPrivate = portPrivate;
        this.iPPublic = iPPublic;
        this.portPublic = portPublic;
        this.ipDest = ipDest;
        this.portDest = portDest;
        this.phoneNumber = phoneNumber;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getiPPrivate() {
        return iPPrivate;
    }

    public void setiPPrivate(String iPPrivate) {
        this.iPPrivate = iPPrivate;
    }

    public String getPortPrivate() {
        return portPrivate;
    }

    public void setPortPrivate(String portPrivate) {
        this.portPrivate = portPrivate;
    }

    public String getiPPublic() {
        return iPPublic;
    }

    public void setiPPublic(String iPPublic) {
        this.iPPublic = iPPublic;
    }

    public String getPortPublic() {
        return portPublic;
    }

    public void setPortPublic(String portPublic) {
        this.portPublic = portPublic;
    }

    public String getIpDest() {
        return ipDest;
    }

    public void setIpDest(String ipDest) {
        this.ipDest = ipDest;
    }

    public String getPortDest() {
        return portDest;
    }

    public void setPortDest(String portDest) {
        this.portDest = portDest;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
