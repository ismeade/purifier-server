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
 *
 * Created by ismeade on 2014/12/9.
 */
public class UpPasswordHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_ORI_PASSWORD = "oriPassword";
    private final static String KEY_NEW_PASSWORD = "newPassword";
    private final static String KEY_CON_PASSWORD = "conPassword";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            // 原始密码
            Object _oriPassword = data.get(KEY_ORI_PASSWORD);
            if (_oriPassword == null || !(_oriPassword instanceof String)) {
                logger.error("oriPassword为空或不是一个String.");
                map.put(KEY_CODE, 1);
                map.put(KEY_MESSAGE, "oriPassword为空或不是一个String.");
                return map;
            }
            String oriPassword = (String) _oriPassword;

            // 新密码
            Object _newPassword = data.get(KEY_NEW_PASSWORD);
            if (_newPassword == null || !(_newPassword instanceof String)) {
                logger.error("newPassword为空或不是一个String.");
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "newPassword为空或不是一个String.");
                return map;
            }
            String newPassword = (String) _newPassword;

            // 确认密码
            Object _conPassword = data.get(KEY_CON_PASSWORD);
            if (_conPassword == null || !(_conPassword instanceof String)) {
                logger.error("conPassword为空或不是一个String.");
                map.put(KEY_CODE, 3);
                map.put(KEY_MESSAGE, "conPassword为空或不是一个String.");
                return map;
            }
            String conPassword = (String) _conPassword;

            UserInfoDao service = new UserInfoDao(context);
            UserInfo user = service.getUserInfoByMobile(mobile);
            if (user == null) {
                logger.error("用户不存在.");
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "用户不存在.");
                return map;
            }
            String passwordMd5 = StringUtils.MD5(mobile + oriPassword);
            if (passwordMd5.equals(user.getPassword())) {
                if ("".equals(newPassword.trim())) {
                    logger.error("newPassword为空.");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "新密码不能为空.");
                    return map;
                }

                if (!newPassword.trim().equals(conPassword.trim())) {
                    logger.error("确认密码与新密码不同。n=" + newPassword.trim(), " c=" + conPassword.trim());
                    map.put(KEY_CODE, 3);
                    map.put(KEY_MESSAGE, "新密码不能为空.");
                    return map;
                }

                user = service.updateUserInfo(user, StringUtils.MD5(mobile + newPassword));
                if (user == null) {
                    logger.error("修改密码失败.");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "修改密码时出错.");
                    return map;
                }


                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "修改成功.");
                return map;
            } else {
                logger.error("原密码错误.");
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "原密码错误.");
                return map;
            }
        } else {
            logger.error("upPasswordReq信息结构错误.");
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "upPasswordReq信息结构错误.");
            return map;
        }
    }

}