package com.github.gxhunter.rpc.common.extension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpiUtil {
    public static <S> S getInstance(Class<S> type) {
        if (type == null) {
            throw new IllegalArgumentException("类型不能为空");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("只能通过接口获取spi实现");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("只能获取添加@SPI注解的实现");
        }
        ServiceLoader<S> serviceLoader = ServiceLoader.load(type);
        return serviceLoader.iterator().next();
    }

}
