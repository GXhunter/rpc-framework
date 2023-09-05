package com.github.gxhunter.rpc.core.serialize.kyro;

import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KryoSerializerTest {

    @Test
    void kryoSerializerTest() {
        RpcRequest target = RpcRequest.builder().methodName("hello")
                .parameters(new Object[]{"sayhelooloo", "sayhelooloosayhelooloo"})
                .interfaceName("github.javaguide.HelloService")
                .paramTypes(new Class<?>[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] bytes = kryoSerializer.serialize(target);
        RpcRequest actual = kryoSerializer.deserialize(bytes, RpcRequest.class);
        assertEquals(target.getRequestId(), actual.getRequestId());
    }
}