package com.github.gxhunter.rpc.core.loadbalance;

import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.common.extension.SPI;

import java.util.List;

/**
 * Interface to the load balancing policy
 *
 * @author hunter
 * @createTime 2023年9月11日
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
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
