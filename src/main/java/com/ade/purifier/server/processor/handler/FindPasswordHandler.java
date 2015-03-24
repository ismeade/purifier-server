package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.dao.UserInfoDao;
import com.ade.purifier.orm.model.UserInfo;
import com.ade.purifier.server.sms.SMSManager;
import com.ade.purifier.server.sms.SMSTask;
import com.ade.purifier.utils.StringUtils;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ismeade on 2014/12/9.
 */
public class FindPasswordHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_OPT  = "opt";

    private final static String KEY_CHECKCODE = "checkCode";
    private final static String KEY_NEWPASSWORD = "newPassword";
    private final static String KEY_CONPASSWORD = "conPassword";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _opt = data.get(KEY_OPT);
            if (_opt == null || !(_opt instanceof Integer)) {
                logger.error("opt为空或不是数。");
                map.put(KEY_CODE, 1);
                map.put(KEY_MESSAGE, "参数错误.");
                return map;
            }
            int opt = (Integer) _opt;

            // 验证用户是否存在
            UserInfoDao service = new UserInfoDao(context);
            UserInfo user = service.getUserInfoByMobile(mobile);
            if (user == null) {
                logger.error("手机号未注册.");
                map.put(KEY_CODE, -1);
                map.put(KEY_MESSAGE, "手机号未注册.");
                return map;
            }

            if (opt == 0) {
                String checkCode = SMSManager.getInstance().addSMSTask(mobile);
                if (checkCode == null) {
                    logger.error("验证码发送失败。");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "验证码发送失败。");
                    return map;
                } else {
                    map.put(KEY_CODE, 0);
                    map.put(KEY_MESSAGE, "验证码发送成功");
                    return map;
                }
            } else if(opt == 1) {
                SMSTask task = SMSManager.getInstance().getSMSTask(mobile);
                if (task == null) {
                    logger.error("验证码已超时。");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "验证码已超时，请重新获取验证码。");
                    return map;
                }
                Object _checkCode = data.get(KEY_CHECKCODE);
                if (_checkCode == null || !(_checkCode instanceof String)) {
                    logger.error("checkCode为空或不是字符串。");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "参数错误.");
                    return map;
                }
                String checkCode = (String) _checkCode;
                if (!checkCode.equals(task.getCode())) {
                    logger.error("验证码错误。");
                    map.put(KEY_CODE, 2);
                    map.put(KEY_MESSAGE, "验证码错误。");
                    return map;
                }
                Object _newPassword = data.get(KEY_NEWPASSWORD);
                Object _conPassword = data.get(KEY_CONPASSWORD);

                if (_newPassword == null || _conPassword == null || !_newPassword.equals(_conPassword)) {
                    logger.error("密码与重复密码错误:" + _newPassword + "," + _conPassword);
                    map.put(KEY_CODE, 3);
                    map.put(KEY_MESSAGE, "密码与重复密码不一致。");
                    return map;
                }

                String newPassword = _newPassword.toString();
                String passwordMd5 = StringUtils.MD5(mobile + newPassword);
                user = service.updateUserInfo(user, passwordMd5);
                if (user == null) {
                    logger.error("密码更新失败。");
                    map.put(KEY_CODE, 3);
                    map.put(KEY_MESSAGE, "密码更新失败。");
                    return map;
                } else {
                    SMSManager.getInstance().delSMSTask(mobile);
                    map.put(KEY_CODE, 0);
                    map.put(KEY_MESSAGE, "新密码设置成功");
                    return map;
                }
            } else {
                logger.error("不可识别的操作类型:" + opt);
                map.put(KEY_CODE, 1);
                map.put(KEY_MESSAGE, "不可识别的操作类型:" + opt);
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