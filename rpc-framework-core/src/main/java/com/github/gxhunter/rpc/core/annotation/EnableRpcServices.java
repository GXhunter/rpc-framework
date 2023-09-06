package com.github.gxhunter.rpc.core.annotation;

import com.github.gxhunter.rpc.core.spring.RpcServerScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * scan custom annotations
 *
 * @author hunter
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServerScannerRegistrar.class)
@Documented
public @interface EnableRpcServices {

    String[] basePackage() default {};

}
