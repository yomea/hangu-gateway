package org.hangu.gateway.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.hangu.gateway.constant.CommonCons;

/**
 * @author wuzhenhong
 * @date 2024/2/5 9:57
 */
@Slf4j
public class ConfigUtils {

    private static volatile Properties PROPERTIES;

    public static Properties getProperties() {
        if (PROPERTIES == null) {
            synchronized (ConfigUtils.class) {
                if (PROPERTIES == null) {
                    String path = System.getProperty(CommonCons.DEF_GATEWAY_KEY);
                    if (path == null || path.length() == 0) {
                        path = System.getenv(CommonCons.DEF_GATEWAY_KEY);
                        if (path == null || path.length() == 0) {
                            path = CommonCons.DEF_GATEWAY_CONFIG;
                        }
                    }
                    PROPERTIES = ConfigUtils.loadProperties(path);
                }
            }
        }
        return PROPERTIES;
    }

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        // 指定了全路径
        if (fileName.startsWith("/")) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                try {
                    properties.load(input);
                } finally {
                    input.close();
                }
            } catch (Throwable e) {
                log.warn(
                    "Failed to load " + fileName + " file from " + fileName + "(ingore this file): " + e.getMessage(),
                    e);
            }
            return properties;
        }

        List<URL> list = new ArrayList<>();
        try {
            // 从类加载器路径加载
            Enumeration<URL> urls = ConfigUtils.getClassLoader(ConfigUtils.class).getResources(fileName);
            list = new ArrayList<>();
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
        } catch (Throwable t) {
            log.warn("Fail to load " + fileName + " file: " + t.getMessage(), t);
        }

        if (list.size() == 0) {
            log.warn("No " + fileName + " found on the class path.");
            return properties;
        }

        log.info("load " + fileName + " properties file from " + list);

        for (java.net.URL url : list) {
            try {
                Properties p = new Properties();
                InputStream input = url.openStream();
                if (input != null) {
                    try {
                        p.load(input);
                        properties.putAll(p);
                    } finally {
                        try {
                            input.close();
                        } catch (Throwable t) {
                        }
                    }
                }
            } catch (Throwable e) {
                log.warn("Fail to load " + fileName + " file from " + url + "(ingore this file): " + e.getMessage(), e);
            }
        }

        return properties;
    }

    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }

}
