package com.mobifone.bigdata.util;

import com.mobifone.bigdata.HbaseController;
import com.mobifone.bigdata.model.ResponseHTTPCount;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ElasticSearchHTTP {
    private String address;
    private int port;
    private static final Logger logger = Logger.getLogger(ElasticSearchHTTP.class);
    public ElasticSearchHTTP() {
    }

    public ElasticSearchHTTP(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public ResponseHTTPCount GetCount(String timestart,String timeend,int type){
        String q = String.format("{\n" +
                    "    \"query\": {\n" +
                    "        \"bool\": {\n" +
                    "            \"must\": [\n" +
                    "                {\n" +
                    "                    \"range\": {\n" +
                    "                        \"time_response.keyword\": {\n" +
                    "                            \"gt\": \"%s\",\n" +
                    "                            \"lt\": \"%s\"\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"term\": {\n" +
                    "                        \"result\": \"%d\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"term\": {\n" +
                    "                        \"response_code\": \"200\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"must_not\": [],\n" +
                    "            \"should\": []\n" +
                    "        }\n" +
                    "    }\n" +
                    "}",timestart,timeend,type);
        String uri = String.format("http://%s:%d/_count", address, port);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, q);
        Request request = new Request.Builder()
//                .header("Authorization", "Bearer " + token)
                .url(uri)
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            if(response.code()==200){
                JSONObject json = new JSONObject(result);
                return new ResponseHTTPCount(json.getLong("count"),200);
            } else {
                return new ResponseHTTPCount(-1,response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public ResponseHTTPCount GetCount(String timestart, String timeend,int type){
//        try {
//            String q = String.format("{\n" +
//                    "    \"query\": {\n" +
//                    "        \"bool\": {\n" +
//                    "            \"must\": [\n" +
//                    "                {\n" +
//                    "                    \"range\": {\n" +
//                    "                        \"time_response.keyword\": {\n" +
//                    "                            \"gt\": \"%s\",\n" +
//                    "                            \"lt\": \"%s\"\n" +
//                    "                        }\n" +
//                    "                    }\n" +
//                    "                },\n" +
//                    "                {\n" +
//                    "                    \"term\": {\n" +
//                    "                        \"result\": \"%d\"\n" +
//                    "                    }\n" +
//                    "                },\n" +
//                    "                {\n" +
//                    "                    \"term\": {\n" +
//                    "                        \"response_code\": \"200\"\n" +
//                    "                    }\n" +
//                    "                }\n" +
//                    "            ],\n" +
//                    "            \"must_not\": [],\n" +
//                    "            \"should\": []\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}",timestart,timeend,type);
//            String uri = String.format("http://%s:%d/_count", address, port);
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(new URI(uri))
//                    .headers("Content-Type", "application/json")
//                    .timeout(Duration.ofSeconds(1))
//                    .POST(HttpRequest.BodyPublishers.ofString(q))
//                    .build();
////            CompletableFuture<String> response = HttpClient.newBuilder()
////                    .build()
////                    .send(request, HttpResponse.BodyHandlers.ofString());
//            HttpResponse<String> response =
//                    HttpClient.newBuilder()
//                            .build().send(request, HttpResponse.BodyHandlers.ofString());
////            System.out.println(response.body());
//            if(response.statusCode()==200){
//                JSONObject json = new JSONObject(response.body());
//                return new ResponseHTTPCount(json.getLong("count"),200);
//            } else {
//                return new ResponseHTTPCount(-1,response.statusCode());
//            }
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            logger.error(e.getMessage());
//            return null;
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//            return null;
//        } catch (InterruptedException e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
}
