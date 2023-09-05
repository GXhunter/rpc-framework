package com.github.gxhunter.rpc.common.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcClient {

    boolean primary() default true;

    /**
     * @return bean名称
     */
    String[] alias() default {};
}
