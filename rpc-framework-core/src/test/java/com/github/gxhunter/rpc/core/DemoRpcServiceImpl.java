package com.github.gxhunter.rpc.core;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
@Slf4j
@RpcService()
public class DemoRpcServiceImpl implements DemoRpcService {

    @Override
    public String hello() {
        return "hello";
    }
}
