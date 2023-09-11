package com.github.gxhunter.rpc.core.spring;

import java.util.function.BiConsumer;

public interface Monad<T> {
    void then(BiConsumer<String, T> p);

}
