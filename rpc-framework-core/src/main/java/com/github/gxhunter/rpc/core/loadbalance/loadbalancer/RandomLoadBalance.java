package com.github.gxhunter.rpc.core.loadbalance.loadbalancer;

import com.github.gxhunter.rpc.core.loadbalance.AbstractLoadBalance;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of random load balancing strategy
 *
 * @author hunter
 * 
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        int index = ThreadLocalRandom.current().nextInt(serviceAddresses.size());
        return serviceAddresses.get(index);
    }
}
