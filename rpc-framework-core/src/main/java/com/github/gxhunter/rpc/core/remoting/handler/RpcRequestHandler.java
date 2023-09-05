package com.github.gxhunter.rpc.core.remoting.handler;

import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.common.exception.RpcException;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.provider.ServiceProvider;
import com.github.gxhunter.rpc.core.provider.impl.ZkServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 处理 rpcRequest：调用相应的方法，返回方法结果
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] 成功执行方法:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
