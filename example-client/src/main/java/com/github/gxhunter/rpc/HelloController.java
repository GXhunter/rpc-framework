package com.github.gxhunter.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author hunter
 */
@Component
public class HelloController {

    @Autowired
    private HelloService helloService;

    public void test() throws InterruptedException {
        for (int i = 1; i <= 10; i++) {
            Thread.sleep(2000);
            System.out.println("客户端收到服务端相应数据:" + helloService.hello("你好"+i));
        }
    }
}
