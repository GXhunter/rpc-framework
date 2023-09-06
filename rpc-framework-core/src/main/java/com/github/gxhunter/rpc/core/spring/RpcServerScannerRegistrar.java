package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.annotation.RpcService;
import com.github.gxhunter.rpc.core.annotation.EnableRpcServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * scan and filter specified annotations
 *
 * @author hunter
 * 
 */
@Slf4j
public class RpcServerScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        //get the attributes and values ​​of RpcScan annotation
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableRpcServices.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            // get the value of the basePackage property
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[]{ClassUtils.getPackageName(metadata.getClassName())};
        }
        // Scan the RpcService annotation
        ClassPathBeanDefinitionScanner rpcServiceScanner = new ClassPathBeanDefinitionScanner(registry);
        rpcServiceScanner.addIncludeFilter(new AnnotationTypeFilter(RpcService.class));
        rpcServiceScanner.setResourceLoader(resourceLoader);
        int rpcServiceCount = rpcServiceScanner.scan(rpcScanBasePackages);
        log.info("rpcServiceScanner扫描的数量 [{}]", rpcServiceCount);

    }
  
}
