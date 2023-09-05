package com.github.gxhunter.rpc.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author hunter
 * @createTime 2023年9月11日
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
