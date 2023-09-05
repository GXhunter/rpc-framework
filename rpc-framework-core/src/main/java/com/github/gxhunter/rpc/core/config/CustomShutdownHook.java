package com.github.gxhunter.rpc.core.config;

import com.github.gxhunter.rpc.core.registry.zk.ZookeeperOperator;
import com.github.gxhunter.rpc.core.remoting.transport.server.NettyRpcServer;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.common.utils.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 服务退出时，清除zk上注册信息，关闭线程池
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@Slf4j
public class CustomShutdownHook {

    public static void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyRpcServer.PORT);
                SingletonFactory.getInstance(ZookeeperOperator.class).clearRegistry(inetSocketAddress);
            } catch (UnknownHostException ignored) {
            }
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }
}
