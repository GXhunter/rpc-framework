package com.github.gxhunter.rpc.core.codec;

import com.github.gxhunter.rpc.common.enums.CompressTypeEnum;
import com.github.gxhunter.rpc.common.enums.SerializationTypeEnum;
import com.github.gxhunter.rpc.common.extension.SPIFactory;
import com.github.gxhunter.rpc.core.RpcConstants;
import com.github.gxhunter.rpc.core.compress.Compressor;
import com.github.gxhunter.rpc.core.dto.RpcMessage;
import com.github.gxhunter.rpc.core.dto.RpcRequest;
import com.github.gxhunter.rpc.core.dto.RpcResponse;
import com.github.gxhunter.rpc.core.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * custom protocol decoder
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
 * <p>
 * {@link LengthFieldBasedFrameDecoder} is a length-based decoder , used to solve TCP unpacking and sticking problems.
 * </p>
 *
 * @author hunter
 * 
 * @see <a href="https://zhuanlan.zhihu.com/p/95621344">LengthFieldBasedFrameDecoder解码器</a>
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    public RpcMessageDecoder() {
        // lengthFieldOffset: magic code is 4B, and version is 1B, and then full length. so value is 5
        // lengthFieldLength: full length is 4B. so value is 4
        // lengthAdjustment: full length include all data and read 9 bytes before, so the left length is (fullLength-9). so values is -9
        // initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    /**
     * @param maxFrameLength      Maximum frame length. It decide the maximum length of data that can be received.
     *                            If it exceeds, the data will be discarded.
     * @param lengthFieldOffset   Length field offset. The length field is the one that skips the specified length of byte.
     * @param lengthFieldLength   The number of bytes in the length field.
     * @param lengthAdjustment    The compensation value to add to the value of the length field
     * @param initialBytesToStrip Number of bytes skipped.
     *                            If you need to receive all of the header+body data, this value is 0
     *                            if you only want to receive the body data, then you need to skip the number of bytes consumed by the header.
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    //        读取检查魔数,n字节
                    readAndCheckMagicNumber(frame);
                    //        读取并检查版本,1个字节
                    byte version = frame.readByte();
                    if (version != RpcConstants.VERSION) {
                        throw new IllegalAccessException("不兼容的版本:" + version);
                    }
                    //        读取正文全字节长度
                    int fullLength = frame.readInt();
                    //        消息类型
                    byte messageType = frame.readByte();
                    byte codecType = frame.readByte();
                    byte compressType = frame.readByte();
                    int requestId = frame.readInt();
                    RpcMessage rpcMessage = RpcMessage.builder()
                            .codec(codecType)
                            .requestId(requestId)
                            .messageType(messageType).build();
                    if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                        rpcMessage.setData(RpcConstants.PING);
                        return rpcMessage;
                    }
                    if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                        rpcMessage.setData(RpcConstants.PONG);
                        return rpcMessage;
                    }
                    int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
                    if (bodyLength > 0) {
                        byte[] bs = new byte[bodyLength];
                        frame.readBytes(bs);
                        // decompress the bytes
                        CompressTypeEnum compress = CompressTypeEnum.getCompressByCode(compressType);
                        Compressor compressor = SPIFactory.getInstance(Compressor.class,compress.getCanonicalName());
                        bs = compressor.decompress(bs);
                        // deserialize the object
                        String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
                        log.info("codec name: [{}] ", codecName);
                        Serializer serializer = SPIFactory.getInstance(Serializer.class);
                        if (messageType == RpcConstants.REQUEST_TYPE) {
                            RpcRequest tmpValue = serializer.deserialize(bs, RpcRequest.class);
                            rpcMessage.setData(tmpValue);
                        } else {
                            RpcResponse tmpValue = serializer.deserialize(bs, RpcResponse.class);
                            rpcMessage.setData(tmpValue);
                        }
                    }
                    return rpcMessage;

                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }

        }
        return decoded;
    }

    private void readAndCheckMagicNumber(ByteBuf in) {
        byte[] tmp = new byte[RpcConstants.MAGIC_NUMBER.length];
        in.readBytes(tmp);
        if (!Arrays.equals(tmp, RpcConstants.MAGIC_NUMBER)) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
        }
    }

}
