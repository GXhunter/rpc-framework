package com.github.gxhunter.rpc.core.spring;

import org.springframework.context.ConfigurableApplicationContext;

public class SpringUtil {
    private static ConfigurableApplicationContext CONTEXT;

    public static void setContext(ConfigurableApplicationContext context) {
        CONTEXT = context;
    }
    public static ConfigurableApplicationContext getContext() {
        return CONTEXT;
    }
}
