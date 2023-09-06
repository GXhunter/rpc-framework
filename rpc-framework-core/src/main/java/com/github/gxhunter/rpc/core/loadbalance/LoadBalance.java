package com.github.gxhunter.rpc.core.loadbalance;

import com.github.gxhunter.rpc.common.extension.SPI;
import com.github.gxhunter.rpc.core.dto.RpcRequest;

import java.util.List;

/**
 * Interface to the load balancing policy
 *
 * @author hunter
 * 
 */
@SPI
public interface LoadBalance {
    /**
     * Choose one from the list of existing service addresses list
     *
     * @param serviceUrlList Service address list
     * @param rpcRequest
     * @return target service address
     */
    String choose(List<String> serviceUrlList, RpcRequest rpcRequest);
}
