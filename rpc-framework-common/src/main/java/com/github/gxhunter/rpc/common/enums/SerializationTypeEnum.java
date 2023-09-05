package com.github.gxhunter.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {

    KYRO((byte) 0x01, "kyro");

    private static final Map<Byte, String> MAP = new ConcurrentHashMap<>();
    private final byte code;
    private final String name;

    static {
        for (SerializationTypeEnum item : SerializationTypeEnum.values()) {
            MAP.put(item.code, item.name);
        }
    }
    public static String getName(byte code) {
        return MAP.get(code);
    }

}
