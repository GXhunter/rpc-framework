package com.github.gxhunter.rpc.core.registry;

import com.github.gxhunter.rpc.common.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author hunter
 * 
 */
@SPI
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param serverName rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(String serverName);
}
