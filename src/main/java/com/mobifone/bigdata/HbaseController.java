package com.mobifone.bigdata;

import com.mobifone.bigdata.api.HbaseAPI;
import com.mobifone.bigdata.common.AppConfig;
import com.mobifone.bigdata.model.*;
import com.mobifone.bigdata.util.ElasticSearchHTTP;
import com.mobifone.bigdata.util.ElasticSearchUtil;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.ConnectException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
public class HbaseController {
    private RequestVTQT data;
    private Boolean connectDB;
    private String regexPhoneNumber = "([84|0]+[3|5|7|8|9])+([0-9]{8})";
    private static final Logger logger = Logger.getLogger(HbaseController.class);
    @GetMapping("/identification")
    public ResponseEntity<?> getResult(Model model) {
        return ResponseEntity.ok("Hello");
    }
    @PostMapping("/historyRequestVTQT")
    @ResponseBody
    public ResponseEntity<?> postHistoryRequestVTQT(@RequestBody RequestHistoryVTQT requestHistoryVTQT) throws IOException {
        String time_start = requestHistoryVTQT.getTimestart();
        String time_end = requestHistoryVTQT.getTimeend();
        JSONObject jsonError = new JSONObject();
        if(time_start==null||time_end==null||time_start.compareToIgnoreCase("")==0||time_end.compareToIgnoreCase("")==0){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Properties appProperties = AppConfig.getAppConfigProperties();
        ElasticSearchHTTP elasHTTP = new ElasticSearchHTTP(appProperties.getProperty("elasticsearch.host"),Integer.parseInt(appProperties.getProperty("elasticsearch.port")));
        long num_success = -1;
        ResponseHTTPCount resSuccess = elasHTTP.GetCount(time_start,time_end,1);
        if (resSuccess == null){
            jsonError.put("status","ERROR");
            jsonError.put("data","Error calling internal Database");
            logger.error("Error calling internal ElasticSearch Database");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonError.toString());
        } else {
            if (resSuccess.getStatusCode()==200){
                num_success = resSuccess.getNumber();
            } else{
                jsonError.put("status","ERROR");
                jsonError.put("data","Error calling internal Database");
                logger.error("Error calling internal ElasticSearch Database");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonError.toString());
            }
        }
        long num_notMatch = -1;
        ResponseHTTPCount resFail = elasHTTP.GetCount(time_start,time_end,0);
        if (resFail == null){
            jsonError.put("status","ERROR");
            jsonError.put("data","Error calling internal Database");
            logger.error("Error calling internal ElasticSearch Database");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonError.toString());
        } else {
            if (resFail.getStatusCode()==200){
                num_notMatch = resFail.getNumber();
            } else{
                jsonError.put("status","ERROR");
                jsonError.put("data","Error calling internal Database");
                logger.error("Error calling internal ElasticSearch Database");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonError.toString());
            }
        }
        ResponseHistoryVTQT responseHistoryVTQT = new ResponseHistoryVTQT(num_success,num_notMatch,200);
        return ResponseEntity.ok(responseHistoryVTQT);
    }
    private TimeLimiter ourTimeLimiter = TimeLimiter.of(TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofMillis(2000)).build());
    @PostMapping("/identificationVTQT1")
    @ResponseBody
    public Callable<ResponseEntity<?>> postResultVTQT1(@RequestBody RequestVTQT requestVTQT) throws IOException {
        return TimeLimiter.decorateFutureSupplier(ourTimeLimiter, () ->
                CompletableFuture.supplyAsync(() -> {
                    Map<String,Object> jsonMap = new HashMap<>();
                    jsonMap.put("time_request",getCurrentLocalDateTimeStamp());
                    HbaseAPI hbaseAPI = HbaseAPI.getInstance();
                    String ipPublic = requestVTQT.getAddress();
                    String port = requestVTQT.getPort();
                    String timeStamp = null;
                    String phoneNumber = requestVTQT.getMsisdn();
                    int portPublic ;
                    if(ipPublic==null||phoneNumber==null||ipPublic.trim().compareToIgnoreCase("")==0||phoneNumber.trim().compareToIgnoreCase("")==0){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    boolean isValid = phoneNumber.matches(regexPhoneNumber);
                    if(!isValid){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    if(port==null||port.compareToIgnoreCase("")==0){
                        portPublic = 0;
                    } else{
                        try{
                            portPublic = Integer.parseInt(port);
                        } catch (Exception e){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                        }
                    }
                    try{
                        if (portPublic==0){
                            try{
                                timeStamp = hbaseAPI.CheckPhoneNumberWithoutPortPublic(ipPublic,phoneNumber);
                            } catch (ConnectException e){
                                System.out.println(e.getMessage());
                            }

                        } else {
                            try{
                                timeStamp = hbaseAPI.CheckPhoneNumberPortPublic(ipPublic,phoneNumber,portPublic);
                            } catch (ConnectException e){
                                System.out.println(e.getMessage());
                            }

                        }
                    } catch (Exception e){
                        JSONObject jsonDataErr = new JSONObject();
                        jsonDataErr.put("message","Fail get data: "+e.getMessage());
                        JSONObject jsonErr = new JSONObject();
                        jsonErr.put("status","ERROR");
                        jsonErr.put("data",jsonDataErr);
                        logger.error("Fail get data: "+e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
                    }
                    if(phoneNumber.compareToIgnoreCase("")==0||ipPublic.compareToIgnoreCase("")==0){
                        ResponseTimesVTQT resVTQT = new ResponseTimesVTQT("NOT_MATCHED", null);
                        jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
                        jsonMap.put("isdn","NULL");
                        jsonMap.put("partner_id",2);
                        jsonMap.put("result",0);
                        jsonMap.put("response_code",200);
                        try{
                            ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
                        } catch (IOException e){
                            JSONObject jsonDataErr = new JSONObject();
                            jsonDataErr.put("message",e.getMessage() + " Database");
                            JSONObject jsonErr = new JSONObject();
                            jsonErr.put("status","ERROR");
                            jsonErr.put("data",jsonDataErr);
//            ResponseError err = new ResponseError("ERROR","Fail to get data to table "+e.getMessage());
                            logger.error("Fail get data: "+e.getMessage() + " Elastic Search");
//            System.out.println(err.getMessage());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
                        }
                        return ResponseEntity.ok(resVTQT);
                    }
                    if (timeStamp!=null){
                        ResponseTimesVTQT resVTQT = new ResponseTimesVTQT("OK",timeStamp, null);
                        jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
                        jsonMap.put("isdn","NULL");
                        jsonMap.put("partner_id",2);
                        jsonMap.put("result",0);
                        jsonMap.put("response_code",200);
                        try{
                            ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
                        } catch (IOException e){
                            JSONObject jsonDataErr = new JSONObject();
                            jsonDataErr.put("message",e.getMessage() + " Database");
                            JSONObject jsonErr = new JSONObject();
                            jsonErr.put("status","ERROR");
                            jsonErr.put("data",jsonDataErr);
                            logger.error("Fail get data: "+e.getMessage() + " Elastic Search");
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
                        }
                        return ResponseEntity.ok(resVTQT);
                    } else {
                        ResponseVTQT resVTQT = new ResponseVTQT("NOT_MATCHED", null);
                        jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
                        jsonMap.put("isdn","NULL");
                        jsonMap.put("partner_id",2);
                        jsonMap.put("result",0);
                        jsonMap.put("response_code",200);
                        try{
                            ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
                        } catch (IOException e){
                            JSONObject jsonDataErr = new JSONObject();
                            jsonDataErr.put("message",e.getMessage() + " Database");
                            JSONObject jsonErr = new JSONObject();
                            jsonErr.put("status","ERROR");
                            jsonErr.put("data",jsonDataErr);
                            logger.error("Fail get data: "+e.getMessage() + " Elastic Search");
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
                        }
                        return ResponseEntity.ok(resVTQT);
                    }
                }));

    }
    @PostMapping("/identificationVTQT")
    @ResponseBody
    public ResponseEntity<?> postResultVTQT(@RequestBody RequestVTQT requestVTQT) throws IOException {
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("time_request",getCurrentLocalDateTimeStamp());
        HbaseAPI hbaseAPI = HbaseAPI.getInstance();
        String ipPublic = requestVTQT.getAddress();
        String port = requestVTQT.getPort();
        String timeStamp = null;
        String phoneNumber = requestVTQT.getMsisdn();
        int portPublic ;
        if(ipPublic==null||phoneNumber==null||ipPublic.trim().compareToIgnoreCase("")==0||phoneNumber.trim().compareToIgnoreCase("")==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        boolean isValid = phoneNumber.matches(regexPhoneNumber);
        if(!isValid){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if(port==null||port.compareToIgnoreCase("")==0){
            portPublic = 0;
        } else{
            try{
                portPublic = Integer.parseInt(port);
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
        try{
            if (portPublic==0){
                try{
                    timeStamp = hbaseAPI.CheckPhoneNumberWithoutPortPublic(ipPublic,phoneNumber);
                } catch (ConnectException e){
                    logger.error(e.getMessage());
                    System.out.println(e.getMessage());
                }

            } else {
                try{
                    timeStamp = hbaseAPI.CheckPhoneNumberPortPublic(ipPublic,phoneNumber,portPublic);
                } catch (ConnectException e){
                    logger.error(e.getMessage());
                    System.out.println(e.getMessage());
                }

            }
        } catch (Exception e){
            JSONObject jsonDataErr = new JSONObject();
            jsonDataErr.put("message","Error connect to internal mapping database after a lot of trying");
            JSONObject jsonErr = new JSONObject();
            jsonErr.put("status","ERROR");
            jsonErr.put("data",jsonDataErr);
            logger.error("Fail connect to Hbase: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
        }
        if(phoneNumber.compareToIgnoreCase("")==0||ipPublic.compareToIgnoreCase("")==0){
            ResponseTimesVTQT resVTQT = new ResponseTimesVTQT("NOT_MATCHED", null);
            jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
            jsonMap.put("isdn","NULL");
            jsonMap.put("partner_id",2);
            jsonMap.put("result",0);
            jsonMap.put("response_code",200);
            try{
                ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
            } catch (IOException e){
                JSONObject jsonDataErr = new JSONObject();
                jsonDataErr.put("message",e.getMessage() + " Database");
                JSONObject jsonErr = new JSONObject();
                jsonErr.put("status","ERROR");
                jsonErr.put("data",jsonDataErr);
                logger.error("Fail get data: "+e.getMessage() + " Elastic Search");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
            }
            return ResponseEntity.ok(resVTQT);
        }
        if (timeStamp!=null){
            ResponseTimesVTQT resVTQT = new ResponseTimesVTQT("OK",timeStamp, null);
            jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
            jsonMap.put("isdn","NULL");
            jsonMap.put("partner_id",2);
            jsonMap.put("result",0);
            jsonMap.put("response_code",200);
            try{
                ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
            } catch (IOException e){
                JSONObject jsonDataErr = new JSONObject();
                jsonDataErr.put("message",e.getMessage() + " Database");
                JSONObject jsonErr = new JSONObject();
                jsonErr.put("status","ERROR");
                jsonErr.put("data",jsonDataErr);
                logger.error("Fail get data: "+e.getMessage() + " Elastic Search");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
            }
            return ResponseEntity.ok(resVTQT);
        } else {
            ResponseVTQT resVTQT = new ResponseVTQT("NOT_MATCHED", null);
            jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
            jsonMap.put("isdn","NULL");
            jsonMap.put("partner_id",2);
            jsonMap.put("result",0);
            jsonMap.put("response_code",200);
            try{
                ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
            } catch (IOException e){
                JSONObject jsonDataErr = new JSONObject();
                jsonDataErr.put("message",e.getMessage() + " Database");
                JSONObject jsonErr = new JSONObject();
                jsonErr.put("status","ERROR");
                jsonErr.put("data",jsonDataErr);
                logger.error("Fail get data: "+e.getMessage() + " Elastic Search");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
            }
            return ResponseEntity.ok(resVTQT);
        }
    }
    public String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    public String getMonth(){
        return LocalDateTime.now().getMonth().toString();
    }
}
