package com.github.gxhunter.rpc.core.client;

import com.github.gxhunter.rpc.common.extension.SPI;
import com.github.gxhunter.rpc.core.dto.RpcRequest;
import com.github.gxhunter.rpc.core.dto.RpcResponse;

import java.util.concurrent.CompletableFuture;

/**
 * 发送RPC请求
 *
 * @author hunter
 */
@SPI
public interface RpcRequestExecutor {
    /**
     * 发送rpc请求
     *
     * @param rpcRequest 请求数据
     * @return 响应结果
     */
    CompletableFuture<RpcResponse<Object>> sendRequest(RpcRequest rpcRequest);
}
