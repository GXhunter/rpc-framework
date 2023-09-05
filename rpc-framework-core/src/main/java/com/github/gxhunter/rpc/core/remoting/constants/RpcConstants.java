package com.github.gxhunter.rpc.core.remoting.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author hunter
 * @createTime 2023年9月11日
 */
public class RpcConstants {


    public static final byte[] MAGIC_NUMBER = {'g', 'r', 'p', 'c'};
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    //版本信息
    public static final byte VERSION = 1;
    public static final byte TOTAL_LENGTH = 16;
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    //ping
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    //pong
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    /**
     * 16字节的固定请求头= 魔数(4) + 版本(1) + 正文全长(4) + 数据类型(1) + 序列化/反序列化器(1) + 压缩器(1) + 自增数(4)
     */
    public static final int HEAD_LENGTH = 16;
    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}
