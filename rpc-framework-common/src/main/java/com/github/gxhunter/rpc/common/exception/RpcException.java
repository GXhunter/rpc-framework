package com.github.gxhunter.rpc.common.exception;

import com.github.gxhunter.rpc.common.enums.RpcErrorMessageEnum;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
