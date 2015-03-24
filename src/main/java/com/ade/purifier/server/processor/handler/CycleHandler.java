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
 * Created by ismeade on 2014/10/9.
 */
public class CycleHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_MAC = "mac";
    private final static String KEY_SLEEP_TIME_STATE = "sleepTimeState";
    private final static String KEY_SLEEP_BTIME = "sleepBTime";
    private final static String KEY_SLEEP_ETIME = "sleepETime";
    private final static String KEY_OUT_TIME_STATE = "outTimeState";
    private final static String KEY_OUT_BTIME = "leftBTime";
    private final static String KEY_OUT_ETIME = "leftETime";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

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
                Object _sleepTimeState = data.get(KEY_SLEEP_TIME_STATE);
                int sleepTimeState = 0;
                int sleepBTime = 0;
                int sleepETime = 0;
                if (_sleepTimeState != null && (_sleepTimeState instanceof Integer)) {
                    sleepTimeState = (Integer) _sleepTimeState;
                }
                if (sleepTimeState == 1) {
                    Object _sleepBTime = data.get(KEY_SLEEP_BTIME);
                    Object _sleepETime = data.get(KEY_SLEEP_ETIME);
                    if (_sleepBTime != null && _sleepETime != null && _sleepBTime instanceof Integer && _sleepETime instanceof Integer) {
                        sleepBTime = (Integer) _sleepBTime;
                        sleepETime = (Integer) _sleepETime;

                    }
                }
                byte[] instruction1 = MsgUtils.createAcpInstruction((byte) 0x9A, (byte) (sleepBTime/256), (byte) (sleepBTime%256), (byte) (sleepETime/256), (byte) (sleepETime%256));
//                byte[] instruction1 = MsgUtils.createAcpInstruction((byte) 0x9A, (byte) (sleepBTime/256), (byte) ((sleepBTime%256)/16), (byte) (sleepBTime%16), (byte) (sleepETime/256), (byte) ((sleepETime%256)/16), (byte) (sleepETime%16));
                acp.send(instruction1);

                Object _outTimeState = data.get(KEY_OUT_TIME_STATE);
                int outTimeState = 0;
                int outBTime = 0;
                int outETime = 0;
                if (_outTimeState != null && (_outTimeState instanceof Integer)) {
                    outTimeState = (Integer) _outTimeState;
                }
                if (outTimeState == 1) {
                    Object _outBTime = data.get(KEY_OUT_BTIME);
                    Object _outETime = data.get(KEY_OUT_ETIME);
                    if (_outBTime != null && _outETime != null && _outBTime instanceof Integer && _outETime instanceof Integer) {
                        outBTime = (Integer) _outBTime;
                        outETime = (Integer) _outETime;
                    }
                }

                byte[] instruction2 = MsgUtils.createAcpInstruction((byte) 0x9B, (byte) (outBTime/256), (byte) (outBTime%256), (byte) (outETime/256), (byte) (outETime%256));
//                byte[] instruction2 = MsgUtils.createAcpInstruction((byte) 0x9B, (byte) (outBTime/256), (byte) ((outBTime%256)/16), (byte) (outBTime%16), (byte) (outETime/256), (byte) ((outETime%256)/16), (byte) (outETime%16));
                acp.send(instruction2);
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "设置已下发.");
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