package com.mobifone.bigdata;

import com.mobifone.bigdata.common.AppConfig;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BigdataApplication {
    private static final Logger logger = Logger.getLogger(BigdataApplication.class);
    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure(AppConfig.getConfLocation() + "/" + "log4j-ddtbapi.properties");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SpringApplication.run(BigdataApplication.class, args);
    }
}
