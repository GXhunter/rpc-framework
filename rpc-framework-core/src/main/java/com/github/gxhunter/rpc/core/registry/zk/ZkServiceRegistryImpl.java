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
    private final ZookeeperOperator mZookeeperOperator = SingletonFactory.getInstance(ZookeeperOperator.class);
    @Override
    public void register(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = ZookeeperOperator.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        mZookeeperOperator.createPersistentNode(servicePath);
    }
    @Override
    public void deregister(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = ZookeeperOperator.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        mZookeeperOperator.deletePersistentNode(servicePath);
    }

}
