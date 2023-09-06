package com.github.gxhunter.rpc.core.remoting.transport;

import com.github.gxhunter.rpc.common.extension.SPI;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.core.remoting.dto.RpcResponse;

import java.util.concurrent.CompletableFuture;

/**
 * send RpcRequest。
 *
 * @author hunter
 */
@SPI
public interface RpcRequestTransport {
    /**
     * 发送rpc请求
     *
     * @param rpcRequest 请求数据
     * @return 响应结果
     */
    CompletableFuture<RpcResponse<Object>> sendRpcRequest(RpcRequest rpcRequest);
}
