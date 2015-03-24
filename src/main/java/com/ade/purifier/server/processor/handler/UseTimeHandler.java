package com.ade.purifier.server.processor.handler;

import com.ade.purifier.server.acp.entity.VirtualAcp;
import com.ade.purifier.server.acp.entity.VirtualAcpManager;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ismeade on 2014/9/21.
 */
public class UseTimeHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_MAC = "mac";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _mac = data.get(KEY_MAC);
            if (_mac == null || !(_mac instanceof String)) {
                map.put(KEY_CODE, 1);
                map.put(KEY_MESSAGE, "mac为空或不是String.");
                return map;
            }
            String mac = (String) _mac;
            VirtualAcp acp = VirtualAcpManager.getVirtualAcp(mac);

            return null;
        } else {
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "registerReq信息结构错误.");
            return map;
        }
    }
}
