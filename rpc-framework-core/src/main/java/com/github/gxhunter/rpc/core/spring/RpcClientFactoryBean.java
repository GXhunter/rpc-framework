package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.enums.RpcErrorMessageEnum;
import com.github.gxhunter.rpc.common.enums.RpcResponseCodeEnum;
import com.github.gxhunter.rpc.common.exception.RpcException;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.core.remoting.dto.RpcResponse;
import com.github.gxhunter.rpc.core.remoting.transport.RpcRequestTransport;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Setter
public class RpcClientFactoryBean implements FactoryBean<Object>, InvocationHandler {
    private static final String INTERFACE_NAME = "interfaceName";
    private Class<?> type;
    private RpcRequestTransport rpcRequestTransport;

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, this);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault() || method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .build();
        log.debug("--------------------------");
        log.debug("调用远程方法\t{}({})", rpcRequest.getInterfaceName(),
                Arrays.stream(rpcRequest.getParamTypes()).map(e -> e.getName() + ".class").collect(Collectors.joining(",")));
        log.debug("--------------------------");
        CompletableFuture<RpcResponse<Object>> completableFuture = rpcRequestTransport.sendRpcRequest(rpcRequest);
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
