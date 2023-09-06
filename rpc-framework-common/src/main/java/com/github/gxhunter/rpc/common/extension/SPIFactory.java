package com.github.gxhunter.rpc.common.extension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SPIFactory {
    private static final Map<Class<?>, Object> MAP = new ConcurrentHashMap<>();

    public static <S> S getInstance(@NonNull Class<S> type) {
        return getInstance(type, null);
    }
    public static <S> S getInstance(@NonNull Class<S> type, String canonicalName) {
        if (MAP.containsKey(type)) {
            return (S) MAP.get(type);
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("只能通过接口获取spi实现");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("只能获取添加@SPI注解的实现");
        }
        ServiceLoader<S> serviceLoader = ServiceLoader.load(type);
        for (S item : serviceLoader) {
            if (canonicalName == null || item.getClass().getCanonicalName().equals(canonicalName)) {
                MAP.putIfAbsent(type, item);
                return item;
            }
        }
        throw new IllegalStateException("找不到"+canonicalName+"对应实现");
    }

}
