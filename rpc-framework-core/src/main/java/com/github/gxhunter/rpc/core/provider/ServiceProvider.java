package com.github.gxhunter.rpc.core.provider;

/**
 * store and provide service object.
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
public interface ServiceProvider {

    /**
     * @param bean rpc service related attributes
     */
    void addService(Object bean);

    /**
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * @param bean rpc service related attributes
     */
    void publishService(Object bean);

}
