package com.github.gxhunter.rpc;


import com.github.gxhunter.rpc.common.annotation.RpcClient;

/**
 * @author hunter
 * 
 */
@RpcClient(primary = false,alias = "haha")
public interface HelloService {
    String hello(String message);
}
