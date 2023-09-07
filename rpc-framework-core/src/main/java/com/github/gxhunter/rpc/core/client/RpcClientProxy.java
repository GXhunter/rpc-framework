package com.github.gxhunter.rpc.core.client;

import com.github.gxhunter.rpc.common.enums.RpcErrorMessageEnum;
import com.github.gxhunter.rpc.common.enums.RpcResponseCodeEnum;
import com.github.gxhunter.rpc.common.exception.RpcException;
import com.github.gxhunter.rpc.core.dto.RpcRequest;
import com.github.gxhunter.rpc.core.dto.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 动态代理@RpcClient对应的接口，生成实现类
 * @see com.github.gxhunter.rpc.common.annotation.RpcClient
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private static final String INTERFACE_NAME = "interfaceName";
    private final Class<?> type;
    private final RpcRequestExecutor rpcRequestExecutor;

    public RpcClientProxy(Class<?> type, RpcRequestExecutor rpcRequestExecutor) {
        this.type = type;
        this.rpcRequestExecutor = rpcRequestExecutor;
    }

    public Object getObject() {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, this);
    }

    /**
     * 动态代理 @RpcClient 注解对应接口，实现真正的远程调用请求
     * @see com.github.gxhunter.rpc.common.annotation.RpcClient
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault() || method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getCanonicalName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        log.debug("--------------------------");
        log.debug("调用远程方法\t{}({})", rpcRequest.getInterfaceName(),
                Arrays.stream(rpcRequest.getParamTypes()).map(e -> e.getCanonicalName() + ".class").collect(Collectors.joining(",")));
        log.debug("--------------------------");
        CompletableFuture<RpcResponse<Object>> completableFuture = rpcRequestExecutor.sendRequest(rpcRequest);
        RpcResponse<Object> rpcResponse = completableFuture.get();
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }

    /**
     * 检查返回结果
     *
     * @param rpcResponse 返回结果
     * @param rpcRequest  请求
     */
    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }

    @Override
    public String toString() {
        return type.getName() + "@" + Integer.toHexString(hashCode());
    }
}
