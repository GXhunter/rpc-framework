package com.github.gxhunter.rpc;


import com.github.gxhunter.rpc.common.annotation.RpcClient;

/**
 * @author hunter
 * 
 */
@RpcClient
public interface HelloService {
    String hello(String message);
}
