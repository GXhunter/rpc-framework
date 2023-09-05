package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.annotation.RpcClient;
import com.github.gxhunter.rpc.common.extension.SpiUtil;
import com.github.gxhunter.rpc.core.annotation.EnableRpcServices;
import com.github.gxhunter.rpc.core.remoting.transport.RpcRequestTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * scan and filter specified annotations
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@Slf4j
public class RpcClientScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        //get the attributes and values ​​of RpcScan annotation
        AnnotationAttributes rpcScanAnnotationAttributes
                = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableRpcServices.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            // get the value of the basePackage property
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray("basePackage");
        }
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{ClassUtils.getPackageName(metadata.getClassName())};
        }
        // Scan the RpcService annotation
        ClassPathScanningCandidateComponentProvider rpcClientScanner = new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            {
                this.setResourceLoader(RpcClientScannerRegistrar.this.resourceLoader);
                this.addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
            }

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };

        for (String basePackage : rpcScanBasePackages) {
            for (BeanDefinition beanDefinition : rpcClientScanner.findCandidateComponents(basePackage)) {
                if (beanDefinition instanceof AnnotatedBeanDefinition) {
                    AnnotationMetadata annotationMetadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
                    Assert.isTrue(annotationMetadata.isInterface(), "@RpcClient 只能添加在interface上");
                    AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcClient.class.getName()));
                    registerRpcClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }

    private void registerRpcClient(BeanDefinitionRegistry registry, AnnotationMetadata metadata, AnnotationAttributes attributes) {
        String className = metadata.getClassName();
        Class clazz = ClassUtils.resolveClassName(className, null);
        RpcClientFactoryBean factoryBean = new RpcClientFactoryBean();
        factoryBean.setType(clazz);
        factoryBean.setRpcRequestTransport(SpiUtil.getInstance(RpcRequestTransport.class));
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(clazz, factoryBean::getObject)
                .setPrimary(attributes.getBoolean("primary"))
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                .getBeanDefinition();
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        beanDefinition.setAttribute("rpcClientsRegistrarFactoryBean", factoryBean);
        log.info("注册:{}到spring容器\n", className);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
                attributes.getStringArray("alias").length == 0 ? null : attributes.getStringArray("alias"));
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
