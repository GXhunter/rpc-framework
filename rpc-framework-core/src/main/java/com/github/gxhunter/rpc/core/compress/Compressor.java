package com.github.gxhunter.rpc.core.compress;

import com.github.gxhunter.rpc.common.extension.SPI;

/**
 * 压缩器
 *
 * @author hunter
 * 
 */
@SPI
public interface Compressor {

    /**
     * 压缩字节流
     *
     * @param bytes 待压缩
     * @return 压缩后
     */
    byte[] compress(byte[] bytes);


    /**
     * 解压缩
     *
     * @param bytes 压缩后
     * @return 原始流
     */
    byte[] decompress(byte[] bytes);
}
