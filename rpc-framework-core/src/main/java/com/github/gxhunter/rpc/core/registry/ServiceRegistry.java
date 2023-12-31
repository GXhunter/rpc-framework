package com.github.gxhunter.rpc.core.registry;

import com.github.gxhunter.rpc.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration
 *
 * @author hunter
 * 
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void register(String rpcServiceName, InetSocketAddress inetSocketAddress);

    void deregister(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
