package com.github.gxhunter.rpc.core.provider.impl;

import com.github.gxhunter.rpc.core.provider.BeanProvider;
import com.github.gxhunter.rpc.core.spring.Monad;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hunter
 * 
 */
@Slf4j
public class LocalBeanProvider implements BeanProvider<Object> {
    private final Map<String,Object> mContext;

    public LocalBeanProvider() {
        mContext = new ConcurrentHashMap<>();
    }

    @Override
    public Object getBean(String beanName) {
        return mContext.get(beanName);
    }

    @Override
    public Monad<Object> addBean(String beanName, Object bean) {
        mContext.put(beanName, bean);
        return biConsumer -> biConsumer.accept(beanName,bean);
    }
}
