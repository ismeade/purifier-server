package com.ade.purifier.server.acp.socket.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by ismeade on 2014/8/29.
 */
public class ByteCodecFactory implements ProtocolCodecFactory {

    private ByteDecoder decoder;
    private ByteEncoder encoder;

    public ByteCodecFactory() {
        this.decoder = new ByteDecoder();
        this.encoder = new ByteEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

}
