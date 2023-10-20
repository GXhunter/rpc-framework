package com.github.gxhunter.rpc.core.registry.zk;

import com.github.gxhunter.rpc.common.enums.RpcErrorMessageEnum;
import com.github.gxhunter.rpc.common.exception.RpcException;
import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.codec.loadbalance.LoadBalance;
import com.github.gxhunter.rpc.core.registry.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author hunter
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;
    private final ZookeeperOperator zookeeperOperator;
    public ZkServiceDiscoveryImpl() {
        this.loadBalance = SPIFactory.getImplement(LoadBalance.class);
        this.zookeeperOperator = SingletonFactory.getInstance(ZookeeperOperator.class);
    }

    @Override
    public InetSocketAddress lookupService(String serverName) {
        List<String> serviceUrlList = zookeeperOperator.getChildrenNodes(serverName);
        if (serviceUrlList == null || serviceUrlList.isEmpty()) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, serverName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.choose(serviceUrlList);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
