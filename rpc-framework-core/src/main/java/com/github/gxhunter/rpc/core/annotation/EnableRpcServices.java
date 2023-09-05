package com.github.gxhunter.rpc.core.annotation;

import com.github.gxhunter.rpc.core.spring.RpcServerScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * scan custom annotations
 *
 * @author hunter
 * @createTime 2023年9月11日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcServerScannerRegistrar.class)
@Documented
public @interface EnableRpcServices {

    String[] basePackage() default {};

}
