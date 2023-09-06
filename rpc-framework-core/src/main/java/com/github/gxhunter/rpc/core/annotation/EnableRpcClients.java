package com.github.gxhunter.rpc.core.annotation;

import com.github.gxhunter.rpc.core.spring.RpcClientScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 参考feign实现
 *
 * @author hunter
 * 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcClientScannerRegistrar.class)
@Documented
public @interface EnableRpcClients {
    /**
     * @return 扫描包
     */
    String[] basePackage() default {};

}
