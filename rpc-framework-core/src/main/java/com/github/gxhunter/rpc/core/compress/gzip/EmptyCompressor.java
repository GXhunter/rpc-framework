package com.github.gxhunter.rpc.core.compress.gzip;

import com.github.gxhunter.rpc.core.compress.Compressor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hunter
 */
@Slf4j
public class EmptyCompressor implements Compressor {

    @Override
    public byte[] compress(byte @NonNull [] bytes) {
        log.info("空压缩");
        return bytes;
    }

    @Override
    public byte[] decompress(byte @NonNull [] bytes) {
        log.info("空解压");
        return bytes;
    }
}
