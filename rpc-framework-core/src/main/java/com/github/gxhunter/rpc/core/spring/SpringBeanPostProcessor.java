package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.provider.ServiceProvider;
import com.github.gxhunter.rpc.core.provider.impl.ZkServiceProviderImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * call this method before creating the bean to see if the class is annotated
 *
 * @author hunter
 * 
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;
    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            serviceProvider.publishService(bean);
        }
        return bean;
    }

}
