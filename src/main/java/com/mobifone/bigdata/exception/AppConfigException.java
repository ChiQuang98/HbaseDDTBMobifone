package com.mobifone.bigdata.exception;

public class AppConfigException extends Exception {
    private static final long serialVersionUID = -6111091783023868367L;

    public AppConfigException() {
        super();
    }

    public AppConfigException(String message) {
        super(message);
    }

    public AppConfigException(Throwable throwable) {
        super(throwable);
    }

    public AppConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }
}