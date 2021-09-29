package com.mobifone.bigdata.util;

import com.mobifone.bigdata.model.ResponseHTTPCount;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ElasticSearchHTTP {
    private String address;
    private int port;

    public ElasticSearchHTTP() {
    }

    public ElasticSearchHTTP(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public ResponseHTTPCount GetCount(String timestart, String timeend,int type){
        try {
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
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .headers("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(1))
                    .POST(HttpRequest.BodyPublishers.ofString(q))
                    .build();
//            CompletableFuture<String> response = HttpClient.newBuilder()
//                    .build()
//                    .send(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> response =
                    HttpClient.newBuilder()
                            .build().send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
            if(response.statusCode()==200){
                JSONObject json = new JSONObject(response.body());
                return new ResponseHTTPCount(json.getLong("count"),200);
            } else {
                return new ResponseHTTPCount(-1,response.statusCode());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
