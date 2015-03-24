package com.ade.purifier.server.processor;

import com.ade.purifier.server.processor.handler.BasicHandler;
import com.ade.purifier.server.processor.handler.HandlerFactory;
import com.ade.purifier.utils.GZipUtil;
import com.ade.purifier.utils.StringUtils;
import org.apache.cayenne.ObjectContext;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ismeade on 2014/8/30.
 */
public class Commissioner {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_VERSION = "version";
    private final static String KEY_SN      = "sn";
    private final static String KEY_DATA    = "data";

    private String        moblie;
    private ObjectContext context;

    Commissioner(String moblie, ObjectContext context) {
        this.moblie  = moblie;
        this.context = context;
    }

    public Map<String, Object> agent(Map<String, Object> message) {
        if (message == null) {
            logger.error("message is Null.");
            return null;
        }
        Map<String, Object> respones = new HashMap<>();
        Map<String, Object> request  = message;
        // version
        Object _version = request.get(KEY_VERSION);
        if (_version != null) {
            respones.put(KEY_VERSION, _version);
        }
        // sn
        Object _sn = request.get(KEY_SN);
        if (_sn != null) {
            respones.put(KEY_SN, _sn);
        }
        // data
        Object _data = request.get(KEY_DATA);
        Object dataMap = null;
        // 解密
        if (_data instanceof String) {
            String date = (String) _data;
            date = GZipUtil.gunzip(date);
            logger.info("解密:" + date);
            dataMap = Json.fromJson(date);
        }
        if (dataMap != null && dataMap instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) dataMap;
            Map<String, Object> reData = null;
            String[] keys = data.keySet().toArray(new String[0]);
            for (String key : keys) {
                if (reData == null) {
                    reData = new HashMap<>();
                    respones.put(KEY_DATA, reData);
                }
                BasicHandler handler = HandlerFactory.newHandler(key);
                Object _one = data.get(key);
                if (handler != null) {
                    Object _reOne = handler.work(moblie, _one, context);
                    String msgType = StringUtils.req2resp(key);
                    logger.info(msgType + ":" + _reOne);
                    reData.put(msgType, _reOne);
                } else {
                    logger.error("创建handler失败:" + key);
                }
            }
            if (reData != null) {
                String _reData = Json.toJson(reData, JsonFormat.compact());
                System.out.println("_reData : " + _reData);
                _reData = GZipUtil.gzip(_reData);
                logger.info("加密:" + _reData);
                respones.put(KEY_DATA, _reData);
            }
        }
        return respones;
    }

    public String getMoblie() {
        return moblie;
    }

    public void setMoblie(String moblie) {
        this.moblie = moblie;
    }

    public ObjectContext getContext() {
        return context;
    }

    public void setContext(ObjectContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
//        String str = "{\"version\":\"1.0-beta\", \"sn\":123456789, \"mobile\":\"13900000000\" \"data\":{\"registrationReq\":{\"password\":\"33221133\",\"email\":\"aaaa@aa.com\"}}}";
//        String str = "{\"version\":\"1.0-beta\", \"sn\":123456789, \"mobile\":\"13900000000\" \"data\":{\"bindMacReq\":{\"mac\":\"A1B2C3D4E5F6\",\"name\":\"test\"}}}";
        String str = "{\"data\":\"H4sIAAAAAAAAAKtWyslPz8wLSi1UsqpWKkgsLi7PL0pRslLKAIIspdpaAH\\/jaqohAAAA\",\"sn\":1418182547228,\"mobile\":\"15000000000\",\"version\":\"1.0-beta\"}";
        Map<String, Object> map = (Map<String, Object>) Json.fromJson(str);
        Commissioner c = CommissionerManager.getCommissioner("15000000000");
        System.out.println(Json.toJson(c.agent(map)));
    }

}
