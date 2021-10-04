package com.mobifone.bigdata.exception;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public enum HttpCode {
        INVALID_INPUT(400),
        BAD_REQUEST(400),
        INTERNAL_ERROR(500),
        NOT_FOUND(404),
        CONFLICT(409),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        DB_ERROR(422),
        BACKEND_ERROR(500);

        private final int statusCode;

        HttpCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }

    private final HttpCode httpCode;
    private final String bizCode;

    public ApplicationException(String msg, Throwable t) {
        this(HttpCode.INTERNAL_ERROR, ExceptionConstant.INTERNAL_ERROR_CODE, msg, t);
    }

    public ApplicationException(HttpCode httpCode, String bizCode, String msg, Throwable t) {
        super(msg, t);
        this.httpCode = httpCode;
        this.bizCode = bizCode;
    }

    public ApplicationException(HttpCode httpCode, String bizCode, Throwable t) {
        super(httpCode.name(), t);
        this.httpCode = httpCode;
        this.bizCode = bizCode;
    }

    public ApplicationException(HttpCode httpCode, String bizCode, String message) {
        super(message);
        this.httpCode = httpCode;
        this.bizCode = bizCode;
    }

    public int getHttpStatusCode() {
        return this.httpCode.getStatusCode();
    }

    public String getBusinessCode() {
        return bizCode;
    }

    public HttpCode getCode() {
        return this.httpCode;
    }

    public String getTrace() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        this.printStackTrace(ps);
        ps.flush();
        return new String(baos.toByteArray());
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("code", httpCode.name());
        map.put("message", super.getMessage());
        return map;
    }
}

