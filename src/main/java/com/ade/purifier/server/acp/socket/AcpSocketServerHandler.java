package com.ade.purifier.server.acp.socket;

import com.ade.purifier.server.acp.entity.VirtualAcp;
import com.ade.purifier.server.acp.entity.VirtualAcpManager;
import com.ade.purifier.server.processor.handler.AcpLoginHandler;
import com.ade.purifier.utils.ByteUtils;
import com.ade.purifier.utils.MessageUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ismeade on 2014/8/29.
 */
public class AcpSocketServerHandler extends IoHandlerAdapter {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static byte MSG_TYPE_LOGIN = (byte) 0x31;
    private final static byte MSG_TYPE_HEART = (byte) 0x32;

    private final static byte DATA_SUCCESS   = (byte) 0x01;
    private final static byte DATA_ERROR     = (byte) 0x02;
    private final static byte DATA_EXCEPTION = (byte) 0x03;

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info("来自客户端 : " + session + " 的连接.");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("close " + session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        if (message instanceof byte[]) {
            byte[] b = (byte[]) message;

            VirtualAcp virtualAcp = VirtualAcpManager.getVirtualAcp(session.getId());
            if (virtualAcp != null) {
                logger.info("收到设备" + virtualAcp.getMac() + "发来的消息:" + ByteUtils.toStr(b));
            } else {
                logger.info("收到未注册设备发来的消息:" + ByteUtils.toStr(b));
            }

            if (MessageUtils.isFormat(b)) {
                long id = session.getId();
                byte type = b[2];
                switch (type) {
                    case MSG_TYPE_LOGIN:
                        String mac = ByteUtils.toStr(MessageUtils.getDatas(b));
                        String md5 = AcpLoginHandler.getMd5ByMac(mac);
                        if (md5 != null) {
                            VirtualAcpManager.addVirtualAcp(md5, session);
                            session.write(new byte[]{MessageUtils.HEAD, (byte) 0x02, MSG_TYPE_LOGIN, DATA_SUCCESS, (byte)0x00, (byte)0x00, MessageUtils.END});
                        } else {
                            session.write(new byte[]{MessageUtils.HEAD, (byte) 0x02, MSG_TYPE_LOGIN, DATA_ERROR, (byte)0x00, (byte)0x00, MessageUtils.END});
                        }
                        break;
                    case MSG_TYPE_HEART:
                        if (VirtualAcpManager.getVirtualAcp(id) != null) {
                            session.write(new byte[]{MessageUtils.HEAD, (byte) 0x02, MSG_TYPE_HEART, DATA_SUCCESS, (byte)0x00, (byte)0x00, MessageUtils.END});
                        } else {
                            session.write(new byte[]{MessageUtils.HEAD, (byte) 0x02, MSG_TYPE_HEART, DATA_ERROR, (byte)0x00, (byte)0x00, MessageUtils.END});
                        }
                        break;
                    default:
                        logger.info("virtualAcp - " + virtualAcp);
                        if (virtualAcp != null) {
                            virtualAcp.setResults(message);
                        }
                        break;
                }

            } else {
                logger.error("message format wrong.");
            }
        } else {
            logger.error("message not instanceof byte[].");
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error(session.toString(), cause);
        VirtualAcpManager.removeVirtualAcp(session.getId());
    }
}
