package org.egg.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/7 18:07
 */
public class PropertiesUtil {
    private static final String path = "/config.properties";
    public static final Resource resource = new ClassPathResource(path);
    public  static Properties props=null;
    static {
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定key的value
     * @param key
     * @return
     */
    public static String getProperty(String key)  {
        return props.getProperty(key);
    }
}
