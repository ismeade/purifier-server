package com.ade.purifier.server.acp.socket.codec;

import com.ade.purifier.utils.ByteUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by ismeade on 2014/8/29.
 */
public class ByteDecoder extends ProtocolDecoderAdapter {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        int limit = ioBuffer.limit();
        byte[] bytes = new byte[limit];
        ioBuffer.get(bytes);
        byte[] realBytes = decrypt(bytes);
        if (realBytes != null) {
            protocolDecoderOutput.write(realBytes);
        }

    }

    private byte[] decrypt(byte[] bytes) {
        logger.info("解密:" + ByteUtils.toStr(bytes));
        ByteSecret.decrypt(bytes);
        logger.info("解密后:" + ByteUtils.toStr(bytes));
        byte tempChack = ByteSecret.getCheck(bytes);
        byte chack = bytes[bytes.length - 2];
        if (tempChack != chack) {
            logger.error("校验和错误.");
            return null;
        }
        return bytes;
    }

}
