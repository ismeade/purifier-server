package com.ade.purifier.server.sms;

import com.ade.purifier.utils.HttpUtils;
import com.ade.purifier.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * Created by ismeade on 2014/12/31.
 */
public class SMSManager {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(SMSManager.class);

    private static SMSManager instance = new SMSManager();

    private static ConcurrentMap<String, SMSTask> map = new ConcurrentHashMap<>();

    private static String url = SMSProperties.VALUE_URL + "?un=" + SMSProperties.VALUE_UN + "&pwd=" + SMSProperties.VALUE_PWD;

    private SMSManager() {
        Thread t = new Thread(new SMSRobot());
        t.start();
    }

    public static SMSManager getInstance() {
        return instance;
    }

    public String addSMSTask(String mobile) {
        if (mobile == null || "".equals(mobile.trim())) {
            logger.error("mobile is null or \"\"");
            return null;
        }
        int timeout = SMSProperties.VALUE_TIMEOUT;
        if (timeout <= 0 ) {
            logger.error("timeout:" + timeout + " <= 0");
            return null;
        }
        String code = RandomUtils.random() + "";
        SMSTask task = new SMSTask(code, mobile, timeout);
        String text = "【DIT空气净化器】找回密码-验证码：" + task.getCode() + "。 此验证码30分钟后失效。";
        try {
            text = URLEncoder.encode(text, "gb2312");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
        String context = url + "&mobile=" + task.getMobile() + "&msg=" + text;
        String reHttp = HttpUtils.http(context);
        if ("result=1&".equals(reHttp.trim())) {
            map.put(mobile, task);
            return code;
        } else {
            logger.error("短信发送失败: context=" + context + " reHttp=" + reHttp);
            return null;
        }
    }

    public SMSTask getSMSTask(String mobile) {
        if (mobile == null || "".equals(mobile.trim())) {
            logger.error("mobile is null or \"\"");
            return null;
        } else {
            return map.get(mobile);
        }
    }

    public void delSMSTask(String mobile) {
        if (mobile == null || "".equals(mobile.trim())) {
            logger.error("mobile is null or \"\"");
        } else {
            map.remove(mobile);
        }
    }

    class SMSRobot implements Runnable {

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String[] keys = map.keySet().toArray(new String[0]);
                for (String key : keys) {
                    SMSTask task = map.get(key);
                    if (task != null && task.isTimeout()) {
                        map.remove(key);
                    }

                }
            }
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        SMSTask task = new SMSTask("123456", "13911162616", 1800);
        String text = "【DIT空气净化器】找回密码-验证码：" + task.getCode() + "。 此验证码30分钟后失效。";
//        System.out.println(URLEncoder.encode(text, "gb2312"));
        String context = url + "&mobile=" + task.getMobile() + "&msg=" + URLEncoder.encode(text, "gb2312");
        String reHttp = HttpUtils.http(context);
        System.out.println(reHttp);
    }

}
