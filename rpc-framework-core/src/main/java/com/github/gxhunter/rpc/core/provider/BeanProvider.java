package com.github.gxhunter.rpc.core.provider;

import com.github.gxhunter.rpc.core.spring.Monad;

/**
 * store and provide service object.
 *
 * @author hunter
 * 
 */
public interface BeanProvider<T> {

    /**
     * @param beanName rpc service name
     * @return service object
     */
    Object getBean(String beanName);

    Monad<T> addBean(String beanName, T bean);
}
