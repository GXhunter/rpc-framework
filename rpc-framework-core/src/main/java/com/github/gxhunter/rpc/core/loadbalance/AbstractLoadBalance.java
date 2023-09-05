package com.github.gxhunter.rpc.core.loadbalance;

import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;

import java.util.List;

/**
 * Abstract class for a load balancing policy
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (serviceAddresses==null||serviceAddresses.isEmpty()) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest);

}
