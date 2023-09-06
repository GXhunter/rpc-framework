package com.github.gxhunter.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hunter
 *
 */
@AllArgsConstructor
@Getter
public enum CompressTypeEnum {

    GZIP((byte) 0x01, "com.github.gxhunter.rpc.core.compress.gzip.GzipCompressor"),
    EMPTY((byte) 0x02, "com.github.gxhunter.rpc.core.compress.gzip.EmptyCompressor");

    private final byte code;
    private final String canonicalName;

    private static final Map<Byte, CompressTypeEnum> ENUM_MAP;
    static {
        ENUM_MAP = new ConcurrentHashMap<>();
        for (CompressTypeEnum item : CompressTypeEnum.values()) {
            ENUM_MAP.put(item.code, item);
        }
    }

    public static CompressTypeEnum getCompressByCode(byte code) {
        return ENUM_MAP.get(code);
    }

}
