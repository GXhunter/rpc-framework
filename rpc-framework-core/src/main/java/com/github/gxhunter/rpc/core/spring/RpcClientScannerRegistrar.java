package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.annotation.RpcClient;
import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.core.annotation.EnableRpcClients;
import com.github.gxhunter.rpc.core.client.RpcClientProxy;
import com.github.gxhunter.rpc.core.client.RpcRequestExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * scan and filter specified annotations
 *
 * @author hunter
 */
@Slf4j
public class RpcClientScannerRegistrar extends AbstractAnnotationImportBeanDefinitionRegistrar {
    @Override
    public Class<? extends Annotation> getImportBeanAnnotation() {
        return EnableRpcClients.class;
    }
    @Override
    public Class<? extends Annotation> getFilterAnnotation() {
        return RpcClient.class;
    }

    @Override
    protected void registerBeanDefinitions(DefaultListableBeanFactory listableBeanFactory, AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        String className = metadata.getClassName();
        AnnotationAttributes attributes = getAnnotationAttributes(metadata,getFilterAnnotation());
        Class clazz = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(clazz,SPIFactory.getImplement(RpcRequestExecutor.class));
        AbstractBeanDefinition abstractBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz, rpcClientProxy::getObject)
                .setPrimary(attributes.getBoolean("primary"))
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                .getBeanDefinition();
        abstractBeanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        abstractBeanDefinition.setAttribute("rpcClientsRegistrarFactoryBean", rpcClientProxy);
        log.debug("注册:{}到spring容器\n", className);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(abstractBeanDefinition, className,
                attributes.getStringArray("alias").length == 0 ? null : attributes.getStringArray("alias"));
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, listableBeanFactory);
    }
}
