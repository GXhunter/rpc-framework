package com.github.gxhunter.rpc.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageTypeEnum {
    REQUEST_TYPE(1),
    RESPONSE_TYPE(2),
    HEARTBEAT_REQUEST_TYPE(3),
    HEARTBEAT_RESPONSE_TYPE(4),
    ;
    private final int value;
}
