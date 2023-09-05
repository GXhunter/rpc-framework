package com.github.gxhunter.rpc;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import com.github.gxhunter.rpc.core.spring.SpringUtil;
import com.github.gxhunter.rpc.core.annotation.EnableRpcServices;
import com.github.gxhunter.rpc.core.remoting.transport.server.NettyRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 通过@RpcService 注解自动注册服务
 * @see RpcService
 */
@EnableRpcServices
@SpringBootApplication
public class NettyServerMain {
    public static void main(String[] args) {
        // Register service via annotation
        ConfigurableApplicationContext applicationContext = SpringApplication.run(NettyServerMain.class, args);
        SpringUtil.setContext(applicationContext);
        NettyRpcServer nettyRpcServer = applicationContext.getBean(NettyRpcServer.class);
        nettyRpcServer.start();
    }
}
