package com.github.gxhunter.rpc.core.remoting.transport.codec;


import com.github.gxhunter.rpc.common.enums.CompressTypeEnum;
import com.github.gxhunter.rpc.common.enums.SerializationTypeEnum;
import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.core.compress.Compressor;
import com.github.gxhunter.rpc.core.remoting.constants.RpcConstants;
import com.github.gxhunter.rpc.core.remoting.dto.RpcMessage;
import com.github.gxhunter.rpc.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * <p>
 * custom protocol decoder
 * <p>
 * <pre>
 *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *   |                                                                                                       |
 *   |                                         body                                                          |
 *   |                                                                                                       |
 *   |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id）
 * body（object类型数据）
 * </pre>
 *
 * @author hunter
 * 
 * @see <a href="https://zhuanlan.zhihu.com/p/95621344">LengthFieldBasedFrameDecoder解码器</a>
 */

@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf out) {
        try {
//            写入魔数
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
//            写入1字节版本号
            out.writeByte(RpcConstants.VERSION);
            // 留白4个字节给后续填写一个int型数字，表示正文全长
            out.writerIndex(out.writerIndex() + 4);
            byte messageType = rpcMessage.getMessageType();
//            写入消息类型 1字节
            out.writeByte(messageType);
//            写入序列化器 1字节
            out.writeByte(rpcMessage.getCodec());
//            写入压缩方式 1字节
            out.writeByte(CompressTypeEnum.GZIP.getCode());
//            写入自增数字 4字节
            out.writeInt(ATOMIC_INTEGER.getAndIncrement());
            // build full length
            byte[] bodyBytes = null;
            int fullLength = RpcConstants.HEAD_LENGTH;
            // if messageType is not heartbeat message,fullLength = head length + body length
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                    && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                // serialize the object
                String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
                log.info("codec name: [{}] ", codecName);
                Serializer serializer = SPIFactory.getInstance(Serializer.class);
                bodyBytes = serializer.serialize(rpcMessage.getData());
                // compress the bytes
                String compressName = CompressTypeEnum.getName(rpcMessage.getCompress());
                Compressor compressor = SPIFactory.getInstance(Compressor.class)
                        ;
                bodyBytes = compressor.compress(bodyBytes);
                fullLength += bodyBytes.length;
            }

            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
//            暂存当前写入位置
            int writeIndex = out.writerIndex();
//            回头填写head部分数据
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
//            继续当前写入位置
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }

    }


}

