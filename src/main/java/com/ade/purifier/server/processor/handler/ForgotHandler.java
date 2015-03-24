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
 * 忘记密码
 * Created by ismeade on 2014/9/10.
 */
public class ForgotHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_EMAIL  = "email";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _email = data.get(KEY_EMAIL);
            if (_email == null) {
                logger.error("email is null.");
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "email is null.");
                return map;
            }
            if (!(_email instanceof String) || !StringUtils.isEmail((String) _email)) {
                logger.error("email 格式不正确.");
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "email 格式不正确.");
                return map;
            }
            String email = (String) _email;
            UserInfoDao service = new UserInfoDao(context);
            UserInfo user = service.getUserInfoByMobileAndEmail(mobile, email);
            if (user != null) {
                // TODO 忘记密码正确处理方法
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "......");
                return map;
            } else {
                logger.error("用户名与邮箱不匹配.");
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "用户名与邮箱不匹配.");
                return map;
            }
        } else {
            logger.error("ForgotReq信息结构错误.");
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "ForgotReq信息结构错误.");
            return map;
        }
    }

}