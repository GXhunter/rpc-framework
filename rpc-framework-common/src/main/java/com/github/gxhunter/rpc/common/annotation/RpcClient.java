package com.github.gxhunter.rpc.common.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcClient {

    boolean primary() default true;

    /**
     * @return 目标服务名
     */
    String value();

    /**
     * @return bean别名
     */
    String[] alias() default {};
}
