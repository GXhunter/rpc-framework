package com.github.gxhunter.rpc.core.registry.zk;

import com.github.gxhunter.rpc.common.enums.RpcErrorMessageEnum;
import com.github.gxhunter.rpc.common.exception.RpcException;
import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.loadbalance.LoadBalance;
import com.github.gxhunter.rpc.core.registry.ServiceDiscovery;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * service discovery based on zookeeper
 *
 * @author hunter
 * 
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = SPIFactory.getInstance(LoadBalance.class);
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        ZookeeperOperator zookeeperOperator = SingletonFactory.getInstance(ZookeeperOperator.class);
        List<String> serviceUrlList = zookeeperOperator.getChildrenNodes(rpcServiceName);
        if (serviceUrlList==null||serviceUrlList.isEmpty()) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
