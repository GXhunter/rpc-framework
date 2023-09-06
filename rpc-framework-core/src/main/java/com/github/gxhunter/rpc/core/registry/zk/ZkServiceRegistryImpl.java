package com.github.gxhunter.rpc.core.registry.zk;

import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * service registration  based on zookeeper
 *
 * @author hunter
 * 
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = ZookeeperOperator.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        SingletonFactory.getInstance(ZookeeperOperator.class).createPersistentNode(servicePath);
    }
}
