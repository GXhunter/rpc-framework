package com.github.gxhunter.rpc.core.registry;

import com.github.gxhunter.rpc.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
