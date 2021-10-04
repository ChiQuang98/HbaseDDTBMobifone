package com.mobifone.bigdata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${hbase.host}")
    private String hbaseHost;
    
    @Value("${hbase.port}")
    private String hbasePort;
    
    @Value("${elasticsearch.host}")
    private String elasticHost;
    
    @Value("${elasticsearch.port}")
    private String elasticPort;

    public String getHbasePort() {
        return hbasePort;
    }

    public String getElasticHost() {
        return elasticHost;
    }

    public String getElasticPort() {
        return elasticPort;
    }

    public String getHbaseHost() {
        return hbaseHost;
    }

    public void setHbaseHost(String host) {
        this.hbaseHost = host;
    }
}
