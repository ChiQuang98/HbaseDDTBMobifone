package com.mobifone.bigdata;

import com.mobifone.bigdata.api.HbaseAPI;
import com.mobifone.bigdata.model.*;
import com.mobifone.bigdata.util.ElasticSearchHTTP;
import com.mobifone.bigdata.util.ElasticSearchUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> postHistoryRequestVTQT(@RequestBody RequestHistoryVTQT requestHistoryVTQT){
        String time_start = requestHistoryVTQT.getTimestart();
        String time_end = requestHistoryVTQT.getTimeend();
        if(time_start==null||time_end==null||time_start.compareToIgnoreCase("")==0||time_end.compareToIgnoreCase("")==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ElasticSearchHTTP elasHTTP = new ElasticSearchHTTP("localhost",9200);
        long num_success = -1;
        ResponseHTTPCount resSuccess = elasHTTP.GetCount(time_start,time_end,1);
        if (resSuccess == null){
            //500
        } else {
            if (resSuccess.getStatusCode()==200){
                num_success = resSuccess.getNumber();
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
                //loi khac
            }
        }
        long num_notMatch = -1;
        ResponseHTTPCount resFail = elasHTTP.GetCount(time_start,time_end,0);
        if (resFail == null){
            //500
        } else {
            if (resFail.getStatusCode()==200){
                num_notMatch = resFail.getNumber();
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
                //loi khac
            }
        }
        ResponseHistoryVTQT responseHistoryVTQT = new ResponseHistoryVTQT(num_success,num_notMatch,200);
        return ResponseEntity.ok(responseHistoryVTQT);
    }
    @PostMapping("/identificationVTQT")
    @ResponseBody
    public ResponseEntity<?> postResultVTQT(@RequestBody RequestVTQT requestVTQT) {
        System.out.println(getCurrentLocalDateTimeStamp());
        System.out.println("Month: "+getMonth());
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
                timeStamp = hbaseAPI.CheckPhoneNumberWithoutPortPublic(ipPublic,phoneNumber);
            } else {
                timeStamp = hbaseAPI.CheckPhoneNumberPortPublic(ipPublic,phoneNumber,portPublic);
            }
        } catch (Exception e){
            System.out.println("INNNN");
            JSONObject jsonErr = new JSONObject();
            JSONObject jsonDataErr = new JSONObject();
            jsonDataErr.put("message","Fail get data: "+e.getMessage());
            jsonErr.put("status","ERROR");
            jsonErr.put("data",jsonDataErr);
//            ResponseError err = new ResponseError("ERROR","Fail to get data to table "+e.getMessage());
            logger.error("Fail get data: "+e.getMessage());
//            System.out.println(err.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonErr.toMap());
        }

        if(phoneNumber.compareToIgnoreCase("")==0||ipPublic.compareToIgnoreCase("")==0){
            ResponseTimesVTQT resVTQT = new ResponseTimesVTQT("NOT_MATCHED", null);
            jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
            jsonMap.put("isdn","NULL");
            jsonMap.put("partner_id",2);
            jsonMap.put("result",0);
            jsonMap.put("response_code",200);
            ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
            return ResponseEntity.ok(resVTQT);
        }
        if (timeStamp!=null){
            ResponseTimesVTQT resVTQT = new ResponseTimesVTQT("OK",timeStamp, null);
            jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
            jsonMap.put("isdn","NULL");
            jsonMap.put("partner_id",2);
            jsonMap.put("result",0);
            jsonMap.put("response_code",200);
            ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
            return ResponseEntity.ok(resVTQT);
        } else {
            ResponseVTQT resVTQT = new ResponseVTQT("NOT_MATCHED", null);
            jsonMap.put("time_response", getCurrentLocalDateTimeStamp());
            jsonMap.put("isdn","NULL");
            jsonMap.put("partner_id",2);
            jsonMap.put("result",0);
            jsonMap.put("response_code",200);
            ElasticSearchUtil.getInstance().InsertJson(jsonMap,getMonth().toLowerCase());
            return ResponseEntity.ok(resVTQT);
        }
    }
    @PostMapping("/identificationMVAS")
    @ResponseBody
    public ResponseEntity<?> postResultMVAS(@RequestBody RequestMVAS requestMVAS) throws IOException {
        System.out.println("");
        HbaseAPI hbaseAPI = HbaseAPI.getInstance();
        String ipPublic = requestMVAS.getSub_ip_address();
        String port = requestMVAS.getSub_ip_port();
        String phoneNumber = null;
        int portPublic ;
        String ipDest = requestMVAS.getDestination_address();
        if(port==null||ipPublic==null||port.trim().compareToIgnoreCase("")==0||ipPublic.trim().compareToIgnoreCase("")==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try{
            portPublic = Integer.parseInt(port);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try{
            if (ipDest==null){
                phoneNumber = hbaseAPI.CheckPortPublicWithoutIpDest(ipPublic,portPublic);
            } else {
                phoneNumber = hbaseAPI.CheckPortPublicWithIpDest(ipPublic,portPublic,ipDest);
            }
        } catch (IOException e){
            logger.error("Fail get data: "+e.getMessage());
            ResponseErrorMVAS resErr = new ResponseErrorMVAS("ERROR","Fail get data: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resErr);
        }
        if(portPublic==0||ipPublic.compareToIgnoreCase("")==0){
            ResponseMVAS resMvas = new ResponseMVAS("NOT_MATCHED");
            return ResponseEntity.ok(resMvas);
        }
        if (phoneNumber!=null){
            ResponsePhoneMVAS resPhoneMvas = new ResponsePhoneMVAS("OK",phoneNumber);
            return ResponseEntity.ok(resPhoneMvas);
        } else {
            ResponseMVAS resMvas = new ResponseMVAS("NOT_MATCHED");
            return ResponseEntity.ok(resMvas);
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
