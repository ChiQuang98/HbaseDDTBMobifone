package com.mobifone.bigdata.common;

import com.mobifone.bigdata.exception.AppConfigException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Properties;

public class AppConfig {
    private static final String CONF_PATH = "conf";
    private static final String CONF_FILE = "app.properties";
    private static PropertiesConfiguration propertiesConfiguration = null;
    public static synchronized PropertiesConfiguration getPropertiesConfiguration() throws AppConfigException {
        if (propertiesConfiguration == null) {
            try {
                String configFile = getLocation(CONF_PATH) + "/" + CONF_FILE;
                propertiesConfiguration = new PropertiesConfiguration();
                // auto reload config when content is changed
                propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
                propertiesConfiguration.load(configFile);
            } catch (MalformedURLException | UnsupportedEncodingException | ConfigurationException e) {
                throw new AppConfigException("Can not load app configuration file.", e);
            }
        }

        return propertiesConfiguration;
    }
    public static Properties getAppConfigProperties() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        String boneCPConfigFile = getLocation(CONF_PATH) + "/" + CONF_FILE;

        prop.load(new FileInputStream(boneCPConfigFile));

        return prop;
    }
    public static String getConfLocation() throws MalformedURLException, UnsupportedEncodingException {
        return getLocation(CONF_PATH);
    }
    public static String getLocation(String location) throws MalformedURLException, UnsupportedEncodingException {
        String result = "";

        CodeSource src = AppConfig.class.getProtectionDomain().getCodeSource();

        URL url = new URL(src.getLocation(), location);
        result = URLDecoder.decode(url.getPath(), "utf-8");

        return result;
    }
}
