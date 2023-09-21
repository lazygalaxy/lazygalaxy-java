package com.lazygalaxy.engine.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class PropertiesUtil {
    private static final Logger LOGGER = LogManager.getLogger(PropertiesUtil.class);

    private static Properties properties = new Properties();

    static {

        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("lazygalaxy.properties"));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(-1);
        }
    }

    public static String getMongoDBURI() {
        return properties.getProperty("mongodb.uri");
    }

    public static String getLeonardoToken() {
        return properties.getProperty("leonardo.token");
    }
}
