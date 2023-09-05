package com.github.gxhunter.rpc.serviceimpl;

import com.github.gxhunter.rpc.HelloService;
import com.github.gxhunter.rpc.common.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
@Slf4j
@RpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String message) {
        log.info("HelloServiceImpl收到: {}.", message);
        String result = "服务端收到了:" + message;
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
