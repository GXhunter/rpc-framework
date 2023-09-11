package com.github.gxhunter.rpc.core.provider;

/**
 * store and provide service object.
 *
 * @author hunter
 * 
 */
public interface BeanFactory {

    /**
     * @param beanName bean名称
     * @return 接口实例对象
     */
    Object getBean(String beanName);

    void addBean(String beanName, Object bean);
}
