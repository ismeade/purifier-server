package com.ade.purifier.server.acp.socket.codec;

import com.ade.purifier.utils.ByteUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by ismeade on 2014/8/29.
 */
public class ByteEncoder extends ProtocolEncoderAdapter {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        byte[] bytes = (byte[]) o;
        byte[] realBytes = encrypt(bytes);
        IoBuffer buffer = IoBuffer.allocate(realBytes.length);
        buffer.setAutoExpand(true);
        buffer.put(realBytes);
        buffer.flip();
        protocolEncoderOutput.write(buffer);
        protocolEncoderOutput.flush();
        buffer.free();
    }

    private byte[] encrypt(byte[] bytes) {
        bytes[bytes.length - 3] = ByteSecret.getRandom();
        byte chack = ByteSecret.getCheck(bytes);
        bytes[bytes.length - 2] = chack;
        logger.info("加密:" + ByteUtils.toStr(bytes));
        ByteSecret.encrypt(bytes);
        logger.info("加密后:" + ByteUtils.toStr(bytes));
        return bytes;
    }

}
