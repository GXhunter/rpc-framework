package com.github.gxhunter.rpc.common.annotation;


import java.lang.annotation.*;

/**
 *
 * @author hunter
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {
    String value() default "";
}
