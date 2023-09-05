package com.github.gxhunter.rpc.common.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 获取单例对象的工厂类
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> c, Supplier<T> supplier) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        String key = c.toString();
        if (!OBJECT_MAP.containsKey(key)) {
            synchronized (c) {
                if (!OBJECT_MAP.containsKey(key)) {
                    OBJECT_MAP.put(key, supplier.get());
                }
            }
        }
        return c.cast(OBJECT_MAP.get(key));
    }

    public static <T> T getInstance(Class<T> c) {
        return getInstance(c, () -> c.cast(OBJECT_MAP.computeIfAbsent(c.toString(), k -> {
            try {
                Constructor<T> declaredConstructor = c.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        })));
    }
}
