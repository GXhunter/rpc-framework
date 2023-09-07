package com.github.gxhunter.rpc.core.spring;

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
public abstract class AbstractAnnotationImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;
    private Environment environment;

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
        return "basePackage";
    }

    /**
     * 获取注解的属性
     *
     * @param annotationMetadata 注解元数据
     * @return 属性kv
     */
    protected final AnnotationAttributes getAnnotationAttributes(AnnotationMetadata annotationMetadata,Class<? extends Annotation> type) {
        return AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(type.getName()));
    }

    @Override
    public final void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes rpcScanAnnotationAttributes = getAnnotationAttributes(annotationMetadata,getImportBeanAnnotation());
        String[] basePackages = new String[0];
        if (rpcScanAnnotationAttributes != null) {
            // get the value of the basePackage property
            basePackages = rpcScanAnnotationAttributes.getStringArray(getBasePackageAttributeName());
        }
        if (basePackages.length == 0) {
            basePackages = new String[]{ClassUtils.getPackageName(annotationMetadata.getClassName())};
        }

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false, this.environment) {
                    {
                        this.setResourceLoader(AbstractAnnotationImportBeanDefinitionRegistrar.this.resourceLoader);
                        this.addIncludeFilter(new AnnotationTypeFilter(AbstractAnnotationImportBeanDefinitionRegistrar.this.getFilterAnnotation()));
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
