package com.github.gxhunter.rpc;

import com.github.gxhunter.rpc.core.annotation.EnableRpcClients;
import com.github.gxhunter.rpc.core.spring.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableRpcClients
@SpringBootApplication
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(NettyClientMain.class, args);
        SpringUtil.setContext(applicationContext);
        HelloController helloController = applicationContext.getBean(HelloController.class);
        helloController.test();
    }
}
