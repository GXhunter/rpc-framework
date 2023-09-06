package com.github.gxhunter.rpc;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.common.utils.ThreadPoolFactoryUtil;
import com.github.gxhunter.rpc.core.RpcConstants;
import com.github.gxhunter.rpc.core.annotation.EnableRpcServices;
import com.github.gxhunter.rpc.core.registry.zk.ZookeeperOperator;
import com.github.gxhunter.rpc.core.server.NettyRpcServer;
import com.github.gxhunter.rpc.core.spring.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 通过@RpcService 注解自动注册服务
 * @see RpcService
 */
@EnableRpcServices
@SpringBootApplication
public class NettyServerMain {
    public static void main(String[] args) {
//        服务退出时关闭退出注册
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), RpcConstants.SERVER_PORT);
                SingletonFactory.getInstance(ZookeeperOperator.class).clearRegistry(inetSocketAddress);
            } catch (UnknownHostException ignored) {
            }
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));

        // Register service via annotation
        ConfigurableApplicationContext applicationContext = SpringApplication.run(NettyServerMain.class, args);
        SpringUtil.setContext(applicationContext);
        NettyRpcServer nettyRpcServer = applicationContext.getBean(NettyRpcServer.class);
        nettyRpcServer.start();
    }
}
