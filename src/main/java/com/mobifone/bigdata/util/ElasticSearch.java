package com.mobifone.bigdata.util;

import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ElasticSearch {
    private String address;
    private int port;

    public ElasticSearch(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public ElasticSearch() {
    }

    public void SaveLogsRequest(String index, JSONObject logs){
        try {
            String uri = String.format("http://%s:%d/%s/_doc", address, port,index);
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .headers("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(1))
                    .POST(HttpRequest.BodyPublishers.ofString(logs.toString()))
                    .build();
            CompletableFuture<HttpResponse<String>> response = HttpClient.newBuilder()
                    .build()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
