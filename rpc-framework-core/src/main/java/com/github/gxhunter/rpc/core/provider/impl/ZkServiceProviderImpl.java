package com.github.gxhunter.rpc.core.provider.impl;

import com.github.gxhunter.rpc.common.enums.RpcErrorMessageEnum;
import com.github.gxhunter.rpc.common.exception.RpcException;
import com.github.gxhunter.rpc.common.extension.SpiUtil;
import com.github.gxhunter.rpc.core.provider.ServiceProvider;
import com.github.gxhunter.rpc.core.registry.ServiceRegistry;
import com.github.gxhunter.rpc.core.remoting.transport.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = SpiUtil.getInstance(ServiceRegistry.class);
    }

    @Override
    public void addService(Object bean) {
        String rpcServiceName = bean.getClass().getInterfaces()[0].getCanonicalName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, bean);
        log.info("Add service: {} and interfaces:{}", rpcServiceName, bean.getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(Object bean) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(bean);
            serviceRegistry.registerService(bean.getClass().getInterfaces()[0].getCanonicalName(), new InetSocketAddress(host, NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }

}
