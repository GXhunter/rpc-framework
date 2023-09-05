package com.github.gxhunter.rpc.core.registry;

import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@SPI
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
