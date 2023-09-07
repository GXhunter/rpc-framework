package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.annotation.EnableRpcServices;
import com.github.gxhunter.rpc.core.provider.ServiceProvider;
import com.github.gxhunter.rpc.core.provider.impl.ZkServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

/**
 * scan and filter specified annotations
 *
 * @author hunter
 */
@Slf4j
public class RpcServerScannerRegistrar extends AbstractAnnotationImportBeanDefinitionRegistrar {
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    @Override
    public Class<? extends Annotation> getImportBeanAnnotation() {
        return EnableRpcServices.class;
    }

    @Override
    public Class<? extends Annotation> getFilterAnnotation() {
        return RpcService.class;
    }

    @Override
    protected void registerBeanDefinitions(DefaultListableBeanFactory listableBeanFactory, AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
        String className = annotationMetadata.getClassName();
        listableBeanFactory.registerBeanDefinition(className, beanDefinition);
        Object bean = listableBeanFactory.getBean(className);
        log.debug("注册:{}到容器", bean.getClass().getCanonicalName());
        serviceProvider.publishService(bean);
    }

}
