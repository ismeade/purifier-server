package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.dao.UserInfoDao;
import com.ade.purifier.orm.model.UserInfo;
import com.ade.purifier.utils.StringUtils;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 * Created by ismeade on 2014/8/31.
 */
public class RegistrationHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_PASSWORD  = "password";
    private final static String KEY_EMAIL     = "email";

    private final static String KEY_CODE    = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _password = data.get(KEY_PASSWORD);
            if (_password == null || !(_password instanceof String) || "".equals(((String) _password).trim())) {
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "密码为空或不是一个字符串");
                return map;
            }
            Object _email = data.get(KEY_EMAIL);
            if (_email == null || !(_email instanceof String) || "".equals(((String) _email).trim())) {
                map.put(KEY_CODE, 3);
                map.put(KEY_MESSAGE, "邮箱为空或不是一个字符串");
                return map;
            }
            String password = (String) _password;
            String email = (String) _email;
            if (!StringUtils.isEmail(email)) {
                map.put(KEY_CODE, 3);
                map.put(KEY_MESSAGE, "邮箱格式不正确.");
                return map;
            }
            UserInfoDao service = new UserInfoDao(context);
            String passwordMd5 = StringUtils.MD5(mobile + password);
            UserInfo user = service.getUserInfoByMobile(mobile);
            if (user != null) {
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "用户手机号已存在.");
                return map;
            }
            int id = service.createUserInfo(mobile, passwordMd5, email);
            if (id > 0) {
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "创建用户成功");
                return map;
            }
        }
        map.put(KEY_CODE, -1);
        map.put(KEY_MESSAGE, "registerReq信息结构错误.");
        return map;
    }

}
