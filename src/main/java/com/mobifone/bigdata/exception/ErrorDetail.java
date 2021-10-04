package com.mobifone.bigdata.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String message;
	private String status;
	
//	public ErrorDetail(String message, String status) {
//		JSONObject jsonDataErr = new JSONObject();
//        jsonDataErr.put("message",message);
//        
//        JSONObject jsonErr = new JSONObject();
//        jsonErr.put("status",status);
//        jsonErr.put("data",jsonDataErr);
//	}
	
//    private Date timestamp;
//    private String errorCode;
//    private String errorMessage;
//    private String path;
//
//    public Date getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getErrorCode() {
//        return errorCode;
//    }
//
//    public void setErrorCode(String errorCode) {
//        this.errorCode = errorCode;
//    }
//
//    public String getErrorMessage() {
//        return errorMessage;
//    }
//
//    public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }
//
//    public ErrorDetail(Date timestamp, String errorMessage, String errorCode, String path) {
//        super();
//        this.timestamp = timestamp;
//        this.errorMessage = errorMessage;
//        this.errorCode = errorCode;
//        this.path = path;
//    }
//
//    public ErrorDetail(String errorCode, String errorMessage) {
//        super();
//        this.errorCode = errorCode;
//        this.errorMessage = errorMessage;
//    }
//
//    public ErrorDetail() {
//    }

    @Override
    public String toString() {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
