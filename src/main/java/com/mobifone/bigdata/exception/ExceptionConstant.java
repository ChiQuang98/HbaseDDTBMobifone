package com.mobifone.bigdata.exception;

public class ExceptionConstant {


    /**
     * Common business code
     * */
	
	 public static final String ID_NOT_NULL = "ID_NOT_NULL";
	 public static final String ID_NOT_NULL_MES = "Id is not null";
	
    public static final String INTERNAL_ERROR_CODE = "SYSTEM_ERROR";
    public static final String INTERNAL_ERROR_MES = "Unexpected server-side error occurred.";

    public static final String TOKEN_INVALID_CODE = "INVALID_ACCESS_TOKEN";
    public static final String TOKEN_INVALID_MES = "Missing or invalid access token provided.";

    public static final String DATABASE_ERROR_CODE = "DB_CAN_NOT_PROCESS_REQUEST";
    public static final String DATABASE_ERROR_MES = "Database can't process request.";

    public static final String DATA_ACCESS_ERROR_CODE = "DATA_DB_IS_IN_USED";
    public static final String DATA_ACCESS_ERROR_MES = "Data is in used. Database can't process request.";

    public static final String INVALID_INPUT_CODE = "INVALID_INPUT";
    public static final String INVALID_INPUT_MES = "Failed due to malformed JSON or Query parameters.";

    public static final String NOT_FOUND_EMAIL_CODE = "NOT_FOUND_EMAIL";
    public static final String NOT_FOUND_EMAIL_MES = "Not found email %s";
    
    public static final String EMAIL_EXISTED_CODE = "EMAIL_IS_EXISTED";
    public static final String EMAIL_EXISTED_MES = "%s is existed.";
    
    public static final String EMAIL_NOT_NULL = "EMAIL_NOT_NULL";
    public static final String EMAIL_NOT_NULL_MES = "Email is not null";
    
    public static final String PASSWORD_NOT_NULL = "PASSWORD_NOT_NULL";
    public static final String PASSWORD_NOT_NULL_MES = "Password is not null";
    
    
    
    public static final String ENTITY_NOT_FOUND_CODE = "ENTITY_NOT_FOUND";
    public static final String ENTITY_NOT_FOUND_MES = "Not found entity";
    


    /**
     * Authentication code
     * */
    public static final String INVALID_CREDENTIALS_CODE = "INVALID_CREDENTIALS";
    public static final String INVALID_CREDENTIALS_MES = "Failed due to using invalid credentials.";
    
    
    public static final String INCORRECT_PASS = "INCORRECT";
    public static final String INCORRECT_PASS_MES = "Incorrect result oldpassword.";


    public static final String ACCESS_RESOURCE_DENIED_CODE = "ACCESS_RESOURCE_DENIED";
    public static final String ACCESS_RESOURCE_DENIED_MES = "Don't have permission to access resource or perform action.";

    public static final String MAINFLUX_ERROR_CODE = "MAINFLUX_ERROR";
    public static final String MAINFLUX_ERROR_MES = "Mainflux error. Code: %s. Message: %s";

    public static final String OK_HTTP_CLIENT_ERROR_CODE = "OK_HTTP_CLIENT_ERROR_CODE";
    public static final String OK_HTTP_CLIENT_ERROR_MES = "OkHttpClient error. Code: %s. Message: %s";

    public static final String GATEWAY_ERROR_CODE = "GATEWAY ERROR";
    public static final String GATEWAY_ERROR_MES = "Gateway error";


}
