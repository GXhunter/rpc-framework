package com.github.gxhunter.rpc.core.compress.gzip;

import com.github.gxhunter.rpc.core.compress.Compressor;
import com.github.gxhunter.rpc.core.remoting.dto.RpcRequest;
import com.github.gxhunter.rpc.core.serialize.kyro.KryoSerializer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GzipCompressTest {
    @Test
    void gzipCompressTest() {
        Compressor gzipCompressor = new GzipCompressor();
        RpcRequest rpcRequest = RpcRequest.builder().methodName("hello")
                .parameters(new Object[]{"sayhelooloo", "sayhelooloosayhelooloo"})
                .interfaceName("github.javaguide.HelloService")
                .paramTypes(new Class<?>[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] rpcRequestBytes = kryoSerializer.serialize(rpcRequest);
        byte[] compressRpcRequestBytes = gzipCompressor.compress(rpcRequestBytes);
        byte[] decompressRpcRequestBytes = gzipCompressor.decompress(compressRpcRequestBytes);
        assertEquals(rpcRequestBytes.length, decompressRpcRequestBytes.length);
    }


}