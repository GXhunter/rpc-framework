package com.github.gxhunter.rpc.core.provider.impl;

import com.github.gxhunter.rpc.core.provider.BeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hunter
 * 
 */
@Slf4j
public class LocalBeanFactory implements BeanFactory {
    private final Map<String,Object> mContext;

    public LocalBeanFactory() {
        mContext = new ConcurrentHashMap<>();
    }

    @Override
    public Object getBean(String beanName) {
        return mContext.get(beanName);
    }

    @Override
    public void addBean(String beanName, Object bean) {
        mContext.put(beanName, bean);
    }
}
