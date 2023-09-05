package com.github.gxhunter.rpc.core.compress;

import com.github.gxhunter.rpc.common.extension.SPI;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */

@SPI
public interface Compress {

    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}
