package com.github.gxhunter.rpc.core.loadbalance.loadbalancer;

import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.core.DemoRpcService;
import com.github.gxhunter.rpc.core.DemoRpcServiceImpl;
import com.github.gxhunter.rpc.core.loadbalance.LoadBalance;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


class ConsistentHashLoadBalanceTest {
    @Test
    void TestConsistentHashLoadBalance() {
        LoadBalance loadBalance = SPIFactory.getInstance(LoadBalance.class);
        List<String> serviceUrlList = new ArrayList<>(Arrays.asList("127.0.0.1:9997", "127.0.0.1:9998", "127.0.0.1:9999"));

        DemoRpcService demoRpcService = new DemoRpcServiceImpl();

        RpcRequest rpcRequest = RpcRequest.builder()
                .parameters(demoRpcService.getClass().getTypeParameters())
                .interfaceName(DemoRpcService.class.getCanonicalName())
                .requestId(UUID.randomUUID().toString())
                .build();
        String userServiceAddress = loadBalance.choose(serviceUrlList, rpcRequest);
        assertEquals("127.0.0.1:9998", userServiceAddress);
    }
}