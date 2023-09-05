package com.github.gxhunter.rpc.core.remoting.transport;

import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.common.extension.SPI;

/**
 * send RpcRequest。
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@SPI
public interface RpcRequestTransport {
    /**
     * 发送rpc请求
     *
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
