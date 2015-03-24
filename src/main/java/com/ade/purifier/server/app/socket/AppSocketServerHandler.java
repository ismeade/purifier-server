package com.ade.purifier.server.app.socket;

import com.ade.purifier.server.processor.Commissioner;
import com.ade.purifier.server.processor.CommissionerManager;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.nutz.json.Json;
import org.nutz.json.JsonException;
import org.nutz.json.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by ismeade on 2014/8/29.
 */
public class AppSocketServerHandler extends IoHandlerAdapter {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private static final String KEY_PATH = AppSocketServerHandler.class.getResource("/key.private").getPath();

    public void sessionOpened(IoSession session) throws Exception {
        logger.info("来自客户端 : " + session + " 的连接.");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("close " + session);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        logger.info("收到客户端消息:" + message);
        String request = (String) message;
//        request = CryUtil.decrypt(KEY_PATH, request);
//        logger.info("解密后:" + request);
        try {
            Object object = Json.fromJson(request);
            if (object instanceof Map<?, ?>) {
                Map<String, Object> map = (Map<String, Object>) object;
                String mobile = (String) map.get("mobile");
                Commissioner commissioner = CommissionerManager.getCommissioner(mobile);
                Object respones = commissioner.agent(map);
                String resp = Json.toJson(respones, JsonFormat.compact());
                logger.info("发给客户端消息:" + resp);
//                resp = CryUtil.encrypt(KEY_PATH, resp);
//                logger.info("加密后:" + resp);
                session.write(resp);
            }
        } catch (NullPointerException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (JsonException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (ClassCastException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error(session.toString(), cause);
    }
}
