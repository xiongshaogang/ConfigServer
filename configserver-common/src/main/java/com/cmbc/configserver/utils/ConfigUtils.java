package com.cmbc.configserver.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * the util class uses to get the property
 * Created by tongchuan.lin<linckham@gmail.com><br/>
 *
 * @Date 2014/11/12
 * @Time 15:39
 */
public class ConfigUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && value.length() > 0) {
            return value;
        } else {
            value = System.getenv(key);
            if (value != null && value.length() > 0) {
                return value;
            }
        }
        return defaultValue;
    }

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(fileName);
            try {
                properties.load(input);
            } finally {
                input.close();
            }
        } catch (Throwable e) {
            logger.warn("Failed to load " + fileName + " file from " + fileName + "(ingore this file): " + e.getMessage(), e);
        }
        return properties;
    }

    @SuppressWarnings({"unchecked"})
    public static List<String> getConfigServerAddressList(String file,String key){
        String address = ConfigUtils.loadProperties(file).getProperty(key);
        if(StringUtils.isNotBlank(address)){
            String[] addressArray = address.split(Constants.COMMA_SEPARATOR);
            List<String> tmpList = Arrays.asList(addressArray);
            Collections.sort(tmpList);
            return tmpList;
        }
        else
        {
          return Collections.EMPTY_LIST;
        }
    }
}
