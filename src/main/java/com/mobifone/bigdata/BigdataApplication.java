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

//           PropertyConfigurator.configure(AppConfig.getConfLocation() + "/" + "application.properties");
            logger.error("test");

        SpringApplication.run(BigdataApplication.class, args);
    }
}
