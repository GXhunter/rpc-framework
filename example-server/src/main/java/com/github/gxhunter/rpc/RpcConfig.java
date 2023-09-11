package com.github.gxhunter.rpc;

import com.github.gxhunter.rpc.core.server.IRpcServer;
import com.github.gxhunter.rpc.core.server.NettyRpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcConfig {
    @Bean
    public IRpcServer rpcServer(){
        return new NettyRpcServer();
    }
}
