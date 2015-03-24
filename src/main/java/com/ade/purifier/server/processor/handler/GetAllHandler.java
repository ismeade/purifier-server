package com.ade.purifier.server.processor.handler;

import com.ade.purifier.server.acp.entity.VirtualAcp;
import com.ade.purifier.server.acp.entity.VirtualAcpManager;
import com.ade.purifier.utils.ByteUtils;
import com.ade.purifier.utils.MsgUtils;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ismeade on 2014/9/21.
 */
public class GetAllHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_MAC = "mac";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    private final static String KEY_PM               = "pm";
    private final static String KEY_SMELL            = "smell";
    private final static String KEY_TEMP             = "temp";
    private final static String KEY_HUMIDITY         = "humidity";
    private final static String KEY_PEOPLE           = "people";
    private final static String KEY_SPEED            = "speed";
    private final static String KEY_UVSTATE          = "uvState";
    private final static String KEY_NIONS            = "nions";
    private final static String KEY_LOCK             = "lock";
    private final static String KEY_TIMING           = "timing";
    private final static String KEY_WORKSTATE        = "workState";
    private final static String KEY_ACPTYPE          = "acpType";
    private final static String KEY_MAINBORDVERSION  = "mainbordVersion";
    private final static String KEY_SOFTVERSION      = "softVersion";
    private final static String KEY_MAINBORDFAILCODE = "mainbordFailCode";
    private final static String KEY_HEPATIMER        = "hepaTimer";
    private final static String KEY_MACHINETIMER     = "machineTimer";
    private final static String KEY_SLEEPBTIME       = "sleepBTime";
    private final static String KEY_SLEEPETIME       = "sleepETime";
    private final static String KEY_LEFTBTIME        = "leftBTime";
    private final static String KEY_LEFTETIME        = "leftETime";
    private final static String KEY_STANDARD         = "standard";
    private final static String KEY_BRIGHTNESS       = "brightness";
    private final static String KEY_HEPAREMTIME      = "hepaRemTime";

    private final static String KEY_CLEARTARGET      = "clearTarget";
    private final static String KEY_ESPHEPAERROR     = "espHepaError";
    private final static String KEY_UVERROR          = "uvError";
    private final static String KEY_ERRORCODE        = "errorCode";


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
            logger.error(acp + "");
            if (acp == null || !acp.isConnected()) {
                logger.error("mac:" + mac + " 未连接或已断开.");
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "净化器:" + mac.substring(mac.length() - 5, mac.length()) + " 未连接到网路.");
                return map;
            } else {
                byte[] in = MsgUtils.createAcpInstruction((byte) 0x35);
                Object _out = acp.send(in, 10000);
                if (_out == null || !(_out instanceof byte[])) {
                    logger.error("acp响应消息类型异常.");
                    map.put(KEY_CODE, -1);
                    map.put(KEY_MESSAGE, "通讯超时，请确认净化器已连接网络.");
                    return map;
                }
                byte[] out = (byte[]) _out;

                int pm = ByteUtils.makeUint16(out[3], out[4]);
                int smell = out[5];
                int temp = out[6] - 30;
                int humidity = out[7];
                int people = out[8] % 16;

                int speed = out[9] / 16;
                int lock = out[9] % 16;
                int uvState = out[10] / 16;
                int nions = out[10] % 16;
                int timing = out[11];
                int workState = out[12] / 16;
//                int clearTarget = out[12] % 16;
                int clearTarget = out[14];
                int espHepaError = out[13] / 16;
                int uvError      = out[13] % 16;
//                int errorCode    = out[14];
                int errorCode    = 0;

                int hepaTimer = ByteUtils.makeUint32((byte) 0x00, out[15], out[16], out[17]) / 60;
                int heapOrEsp = out[18];
                int hepaRemTime = 1500 - hepaTimer;
                if (heapOrEsp == 2) {
                    hepaRemTime = hepaRemTime - 1100;
                }
                int machineTimer = ByteUtils.makeUint32((byte) 0x00, out[19], out[20], out[21]) / 60;

                int sleepBTime = ByteUtils.makeUint16(out[22], out[23]);
                int sleepETime = ByteUtils.makeUint16(out[24], out[25]);

                int leftBTime = ByteUtils.makeUint16(out[26], out[27]);
                int leftETime = ByteUtils.makeUint16(out[28], out[29]);

                int acpType = 1;
                int mainbordVersion = 1;
                int softVersion = 1;
                int mainbordFailCode = 1;
                int brightness = 1;


                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "成功.");
                map.put(KEY_PM, pm);
                map.put(KEY_TEMP, temp);
                map.put(KEY_SMELL, smell);
                map.put(KEY_HUMIDITY, humidity);
                map.put(KEY_PEOPLE, people);
                map.put(KEY_SPEED, speed);
                map.put(KEY_UVSTATE, uvState);
                map.put(KEY_NIONS, nions);
                map.put(KEY_LOCK, lock);
                map.put(KEY_TIMING, timing);
                map.put(KEY_WORKSTATE, workState);
                map.put(KEY_ACPTYPE, acpType);
                map.put(KEY_MAINBORDVERSION, mainbordVersion);
                map.put(KEY_SOFTVERSION, softVersion);
                map.put(KEY_MAINBORDFAILCODE, mainbordFailCode);
                map.put(KEY_HEPATIMER, hepaTimer);
                map.put(KEY_MACHINETIMER, machineTimer);
                map.put(KEY_SLEEPBTIME, sleepBTime);
                map.put(KEY_SLEEPETIME, sleepETime);
                map.put(KEY_LEFTBTIME, leftBTime);
                map.put(KEY_LEFTETIME, leftETime);
                map.put(KEY_STANDARD, clearTarget);
                map.put(KEY_BRIGHTNESS, brightness);
                map.put(KEY_HEPAREMTIME, hepaRemTime);

                map.put(KEY_CLEARTARGET     , clearTarget);
                map.put(KEY_ESPHEPAERROR, espHepaError);
                map.put(KEY_UVERROR     , uvError     );
                map.put(KEY_ERRORCODE   , errorCode   );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return map;
            }

        } else {
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "registerReq信息结构错误.");
            return map;
        }
    }

}
