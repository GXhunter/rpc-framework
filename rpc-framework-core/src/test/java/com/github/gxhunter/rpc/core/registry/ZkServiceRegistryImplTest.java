package com.github.gxhunter.rpc.core.registry;

import com.github.gxhunter.rpc.core.DemoRpcService;
import com.github.gxhunter.rpc.core.DemoRpcServiceImpl;
import com.github.gxhunter.rpc.core.dto.RpcRequest;
import com.github.gxhunter.rpc.core.registry.zk.ZkServiceDiscoveryImpl;
import com.github.gxhunter.rpc.core.registry.zk.ZkServiceRegistryImpl;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author hunter
 * 
 */
class ZkServiceRegistryImplTest {

    @Test
    void should_register_service_successful_and_lookup_service_by_service_name() {
        ServiceRegistry zkServiceRegistry = new ZkServiceRegistryImpl();
        InetSocketAddress givenInetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);
        DemoRpcService demoRpcService = new DemoRpcServiceImpl();
        zkServiceRegistry.register(DemoRpcService.class.getCanonicalName(), givenInetSocketAddress);
        ServiceDiscovery zkServiceDiscovery = new ZkServiceDiscoveryImpl();
        RpcRequest rpcRequest = RpcRequest.builder()
//                .parameters(args)
                .interfaceName(DemoRpcService.class.getCanonicalName())
//                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        InetSocketAddress acquiredInetSocketAddress = zkServiceDiscovery.lookupService(rpcRequest);
        assertEquals(givenInetSocketAddress.toString(), acquiredInetSocketAddress.toString());
    }
}
