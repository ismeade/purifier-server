package com.ade.purifier.server.processor.handler;

import com.ade.purifier.server.acp.entity.VirtualAcp;
import com.ade.purifier.server.acp.entity.VirtualAcpManager;
import com.ade.purifier.utils.MsgUtils;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ismeade on 2014/9/21.
 */
public class TimingHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_MAC = "mac";
    private final static String KEY_SET = "set";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";
    private final static String KEY_STATE = "state";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _mac = data.get(KEY_MAC);
            if (_mac == null || !(_mac instanceof String)) {
                logger.error("mac为空或不是String.");
                map.put(KEY_CODE, 1);
                map.put(KEY_MESSAGE, "mac为空或不是String.");
                return map;
            }
            String mac = (String) _mac;
            VirtualAcp acp = VirtualAcpManager.getVirtualAcp(mac);
            if (acp == null || !acp.isConnected()) {
                logger.error("mac:" + mac + " 未连接或已断开.");
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "净化器:" + mac.substring(mac.length() - 5, mac.length()) + " 未连接到网路.");
                return map;
            } else {
                Object _set = data.get(KEY_SET);
                if (_set == null || !(_set instanceof Integer)) {
                    logger.error("set为空或不是Integer.");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "set为空或不是Integer.");
                    return map;
                }
                int set = (Integer) _set;
                byte[] instruction = MsgUtils.createAcpInstruction((byte) 0x97, (byte) set);
                Object _resp = acp.send(instruction);
                if (_resp == null || !(_resp instanceof byte[])) {
                    logger.error("acp响应消息类型异常.");
                    map.put(KEY_CODE, -1);
                    map.put(KEY_MESSAGE, "通讯超时，请确认净化器已连接网络.");
                    return map;
                }
                byte[] resp = (byte[]) _resp;
                byte b2 = resp[3];
                map.put(KEY_STATE, b2);
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "操作成功.");
                return map;
            }
        } else {
            logger.error("TimingReq信息结构错误.");
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "TimingReq信息结构错误.");
            return map;
        }
    }

}