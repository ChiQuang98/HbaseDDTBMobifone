package com.mobifone.bigdata;


import com.mobifone.bigdata.api.HbaseAPI;
import com.mobifone.bigdata.model.Request;
import com.mobifone.bigdata.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class HbaseController {
    private Request data;
    private Boolean connectDB;

    @GetMapping("/identification")
    public ResponseEntity<?> getResult(Model model) {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/identification")
    @ResponseBody
    public ResponseEntity<?> postResult(@RequestBody Request request) {
//        data = new Request("0984060798", "10.11.13", "1234");
        HbaseAPI hbaseAPI = HbaseAPI.getInstance();

        String ipPublic = request.getAddress();
        int portPublic = request.getPort();
        String phoneNumber = request.getMsisdn();
        if(phoneNumber.compareToIgnoreCase("")==0||ipPublic.compareToIgnoreCase("")==0){
            Response requestModel1 = new Response("NOT_MATCHED", null);
            return ResponseEntity.ok(requestModel1);
        }
        boolean isValid = hbaseAPI.GetPhoneNumber(ipPublic,portPublic,phoneNumber);
        if (isValid){
            Response requestModel1 = new Response("OK", null);
            return ResponseEntity.ok(requestModel1);
        } else {
            Response requestModel1 = new Response("NOT_MATCHED", null);
            return ResponseEntity.ok(requestModel1);
        }
    }
}
