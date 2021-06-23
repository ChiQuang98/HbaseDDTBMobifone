package com.mobifone.bigdata;

import com.mobifone.bigdata.api.HbaseAPI;
import org.json.JSONObject;
import org.apache.log4j.Logger;


public class TestAPI {

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        JSONObject jsonSub = new JSONObject();
        JSONObject jsonSub1 = new JSONObject();
        jsonSub.put("PhoneNumber1","0975312798");
        jsonSub.put("timestmap","123456");
        jsonSub1.put("PhoneNumber1","0975312798");
        jsonSub1.put("timestmap","123456");
        json.put("Port1",jsonSub);
        json.put("Port2",jsonSub1);

        System.out.println(json.toString());

    }
}
