package com.github.gxhunter.rpc;


import com.github.gxhunter.rpc.common.annotation.RpcClient;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
@RpcClient
public interface HelloService {
    String hello(String message);
}
