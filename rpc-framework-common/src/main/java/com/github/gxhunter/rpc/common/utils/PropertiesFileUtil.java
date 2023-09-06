package com.github.gxhunter.rpc.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author hunter
 * 
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesFileUtil {
    /**
     * @param fileName 从根目录的完整路径名
     * @return 属性
     */
    public static Properties readPropertiesFile(String fileName) {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream in = classLoader.getResourceAsStream(fileName)) {
            properties.load(in);
        } catch (IOException e) {
            log.error("读取配置:{}失败",fileName);
        }
        return properties;
    }
}
