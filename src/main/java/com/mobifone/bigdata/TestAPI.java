package com.mobifone.bigdata;

import com.mobifone.bigdata.api.HbaseAPI;
import org.json.JSONObject;

public class TestAPI {
    public static void main(String[] args) {
        System.out.println(new HbaseAPI().CheckPhoneNumberPortPublic("227.235.252.12","84479493425",33297));
//        JSONObject json = new JSONObject();
//        json.put("a","s");
//        json.put("s")
//        String s = json.toString();
//        System.out.println(s);
    }
}
