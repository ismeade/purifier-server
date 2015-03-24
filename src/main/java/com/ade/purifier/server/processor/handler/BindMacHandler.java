package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.dao.MacInfoDao;
import com.ade.purifier.orm.dao.UserInfoDao;
import com.ade.purifier.orm.dao.UserMacDao;
import com.ade.purifier.orm.model.MacInfo;
import com.ade.purifier.orm.model.UserInfo;
import com.ade.purifier.orm.model.UserMac;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 绑定mac地址
 * Created by ismeade on 2014/9/9.
 */
public class BindMacHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_MAC  = "mac";
    private final static String KEY_NAME     = "name";

    private final static String KEY_CODE    = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _mac = data.get(KEY_MAC);
            if (_mac == null || !(_mac instanceof String) || "".equals(((String) _mac).trim())) {
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "MAC为空或不是一个字符串");
                return map;
            }
            Object _name = data.get(KEY_NAME);
            if (_name == null || !(_name instanceof String) || "".equals(((String) _name).trim())) {
                _name = "";
            }
            String mac = (String) _mac;
            String name = (String) _name;

            UserInfoDao userService = new UserInfoDao(context);
            UserInfo user = userService.getUserInfoByMobile(mobile);
            if (user == null) {
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "账户不存在.");
                return map;
            }
            MacInfoDao macService = new MacInfoDao(context);
            MacInfo macInfo = macService.getMacInfoByMd5(mac);
            if (macInfo == null) {
                map.put(KEY_CODE, -2);
                map.put(KEY_MESSAGE, "没有指定的净化器");
                return map;
            }
            UserMacDao userMacDao = new UserMacDao(context);
            UserMac userMac = userMacDao.getUserMacByMacId(user.getId(), macInfo.getId());
            if (userMac != null) {
                macService.updateMacInfoName(macInfo.getId(), name);
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "绑定成功");
                return map;
            }
            int userMacId = userMacDao.createUserMac(user.getId(), macInfo.getId());
            if (userMacId > 0)  {
                macService.updateMacInfoName(macInfo.getId(), name);
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "绑定成功");
                return map;
            } else {
                map.put(KEY_CODE, -3);
                map.put(KEY_MESSAGE, "绑定失败");
                return map;
            }
        } else {
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "registerReq信息结构错误.");
            return map;
        }

    }

}
