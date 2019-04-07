package com.company.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileResolver {
    private static PropertyFileResolver propertyFileResolver;
    private Properties properties;
    private static final String PROPERTIES_FILE_PATH = "properties.properties";
    private static final String FILES_TO_TRANSFORM_PROPERTY_NAME = "filesToTransformPath";
    private static final String DB_URL_PROPERTY_NAME = "dbUrl";
    private static final String DB_USER_PROPERTY_NAME = "dbUser";
    private static final String DB_PASSWORD_PROPERTY_NAME = "dbPassword";
    private static final Logger log = LoggerFactory.getLogger(FileInformationsReader.class);

    private PropertyFileResolver() {
        properties = new Properties();
    }

    public static synchronized PropertyFileResolver getInstance() {
        if(propertyFileResolver == null) {
            propertyFileResolver = new PropertyFileResolver();
        }
        return propertyFileResolver;
    }

    public String getPropertyValue(String propertyName) {
        if(properties.isEmpty()) {
            this.loadProperties();
        }
        return properties.getProperty(propertyName);
    }

    static String getFilesToTransformPropertyName() {
        return FILES_TO_TRANSFORM_PROPERTY_NAME;
    }

    public static String getDbUrlPropertyName() {
        return DB_URL_PROPERTY_NAME;
    }

    public static String getDbUserPropertyName() {
        return DB_USER_PROPERTY_NAME;
    }

    public static String getDbPasswordPropertyName() {
        return DB_PASSWORD_PROPERTY_NAME;
    }

    private void loadProperties() {
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        } catch (IOException e) {
            log.error("Unable to read properties from file: " + PROPERTIES_FILE_PATH, e);
            throw new IllegalStateException("Unable to read properties from file: " + PROPERTIES_FILE_PATH, e);
        }
    }
}
