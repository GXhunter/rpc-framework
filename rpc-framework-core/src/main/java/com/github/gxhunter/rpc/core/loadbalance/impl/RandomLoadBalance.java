package com.github.gxhunter.rpc.core.loadbalance.impl;

import com.github.gxhunter.rpc.core.loadbalance.LoadBalance;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of random load balancing strategy
 *
 * @author hunter
 * 
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String choose(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (serviceAddresses==null||serviceAddresses.isEmpty()) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }

        int index = ThreadLocalRandom.current().nextInt(serviceAddresses.size());
        return serviceAddresses.get(index);
    }

}
