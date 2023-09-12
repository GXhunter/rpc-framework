package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import com.github.gxhunter.rpc.common.factory.SingletonFactory;
import com.github.gxhunter.rpc.core.RpcConstants;
import com.github.gxhunter.rpc.core.annotation.EnableRpcServices;
import com.github.gxhunter.rpc.core.provider.BeanFactory;
import com.github.gxhunter.rpc.core.provider.impl.LocalBeanFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * scan and filter specified annotations
 *
 * @author hunter
 */
@Slf4j
public class RpcServerScannerRegistrar extends AbstractImportBeanDefinitionRegistrar {
    private final BeanFactory mBeanFactory = SingletonFactory.getInstance(LocalBeanFactory.class);

    @Override
    public Class<? extends Annotation> getImportBeanAnnotation() {
        return EnableRpcServices.class;
    }

    @Override
    public Class<? extends Annotation> getFilterAnnotation() {
        return RpcService.class;
    }

    @SneakyThrows
    @Override
    public void onStartImport(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry, String[] basePackages) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry,false,environment);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        scanner.scan(basePackages);
        String serverName = Optional.ofNullable(annotationMetadata.getAnnotationAttributes(getImportBeanAnnotation().getName()))
                .map(AnnotationAttributes::fromMap)
                .map(attributes -> attributes.getString("serverName"))
                .orElseThrow(IllegalStateException::new);
        RpcConstants.SERVER_NAME.complete(serverName);
        String host = InetAddress.getLocalHost().getHostAddress();
        this.serviceRegistry.register(serverName, new InetSocketAddress(host, RpcConstants.SERVER_PORT));
    }

    @SneakyThrows
    @Override
    protected void registerBeanDefinitions(DefaultListableBeanFactory listableBeanFactory, AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
        String className = Class.forName(annotationMetadata.getClassName()).getInterfaces()[0].getCanonicalName();
        listableBeanFactory.registerBeanDefinition(className, beanDefinition);
        Object bean = listableBeanFactory.getBean(className);
        log.debug("注册:{}到容器", bean.getClass().getCanonicalName());
        mBeanFactory.addBean(className,bean);
    }

}
