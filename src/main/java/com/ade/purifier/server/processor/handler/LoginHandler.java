package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.dao.MacInfoDao;
import com.ade.purifier.orm.dao.UserInfoDao;
import com.ade.purifier.orm.dao.UserMacDao;
import com.ade.purifier.orm.model.MacInfo;
import com.ade.purifier.orm.model.UserInfo;
import com.ade.purifier.orm.model.UserMac;
import com.ade.purifier.utils.StringUtils;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录验证
 * Created by ismeade on 2014/9/10.
 */
public class LoginHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_PASSWORD = "password";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    private final static String KEY_MACLIST = "macList";
    private final static String KEY_MACLIST_MD5 = "md5";
    private final static String KEY_MACLIST_NAME = "name";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _password = data.get(KEY_PASSWORD);
            if (_password == null || !(_password instanceof String)) {
                logger.error("password为空或不是一个String.");
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "password为空或不是一个String.");
                return map;
            }
            String password = (String) _password;
            UserInfoDao service = new UserInfoDao(context);
            UserInfo user = service.getUserInfoByMobile(mobile);
            if (user == null) {
                logger.error("用户不存在.");
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "用户不存在.");
                return map;
            }
            String passwordMd5 = StringUtils.MD5(mobile + password);
            if (passwordMd5.equals(user.getPassword())) {
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "登陆成功.");

                List<Map<String, Object>> macList = new ArrayList<>();
                map.put(KEY_MACLIST, macList);
                UserMacDao userMacDao = new UserMacDao(context);
                MacInfoDao macInfoDao = new MacInfoDao(context);
                List<UserMac> userMacList = userMacDao.getUserMacByUserId(user.getId());
                for (UserMac userMac : userMacList) {
                    MacInfo macInfo =  macInfoDao.getMacInfoById(userMac.getMacId());
                    if (macInfo != null) {
                        Map<String, Object> mac = new HashMap<>();
                        mac.put(KEY_MACLIST_MD5, macInfo.getMd5());
                        mac.put(KEY_MACLIST_NAME, macInfo.getName());
                        macList.add(mac);
                    }
                }
                return map;
            } else {
                logger.error("密码错误.");
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "密码错误.");
                return map;
            }
        } else {
            logger.error("loginReq信息结构错误.");
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "loginReq信息结构错误.");
            return map;
        }
    }

}