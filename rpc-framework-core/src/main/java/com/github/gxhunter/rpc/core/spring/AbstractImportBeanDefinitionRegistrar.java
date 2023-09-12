package com.github.gxhunter.rpc.core.spring;

import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.core.registry.ServiceRegistry;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * 基于注解的bean扫描装载
 */
public abstract class AbstractImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    protected ResourceLoader resourceLoader;
    protected Environment environment;
    public final ServiceRegistry serviceRegistry = SPIFactory.getImplement(ServiceRegistry.class);

    /**
     * @return 导入spring的注解
     */
    protected abstract Class<? extends Annotation> getImportBeanAnnotation();

    /**
     * @return 按注解过滤
     */
    protected abstract Class<? extends Annotation> getFilterAnnotation();

    /**
     * @return 扫描的包
     */
    protected String getBasePackageAttributeName() {
        return "basePackages";
    }

    public void onStartImport(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry, String[] basePackages) {
    }
    @Override
    public final void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes rpcScanAnnotationAttributes
                = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(getImportBeanAnnotation().getName()));
        String[] basePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            // get the value of the basePackage property
            basePackages = rpcScanAnnotationAttributes.getStringArray(getBasePackageAttributeName());
        }
        if (basePackages.length == 0) {
            basePackages = new String[]{ClassUtils.getPackageName(annotationMetadata.getClassName())};
        }
        onStartImport(annotationMetadata, registry,basePackages);

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false, this.environment) {
                    {
                        this.setResourceLoader(AbstractImportBeanDefinitionRegistrar.this.resourceLoader);
                        this.addIncludeFilter(new AnnotationTypeFilter(AbstractImportBeanDefinitionRegistrar.this.getFilterAnnotation()));
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

        for (String basePackage : basePackages) {
            for (BeanDefinition beanDefinition : scanner.findCandidateComponents(basePackage)) {
                if (beanDefinition instanceof AnnotatedBeanDefinition) {
                    registerBeanDefinitions((DefaultListableBeanFactory) registry, (AnnotatedBeanDefinition) beanDefinition);
                }
            }
        }
    }

    /**
     * 注册bean到spring容器中
     *
     * @param listableBeanFactory spring上下文工厂对象
     * @param beanDefinition      bean 数据
     */
    protected abstract void registerBeanDefinitions(DefaultListableBeanFactory listableBeanFactory, AnnotatedBeanDefinition beanDefinition);

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
