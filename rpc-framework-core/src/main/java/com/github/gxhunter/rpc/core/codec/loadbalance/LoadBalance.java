package com.github.gxhunter.rpc.core.codec.loadbalance;

import com.github.gxhunter.rpc.common.extension.SPI;

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
     * @return target service address
     */
    String choose(List<String> serviceUrlList);
}
