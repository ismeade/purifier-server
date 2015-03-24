package com.ade.purifier.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ismeade on 2014/9/9.
 */
public class StringUtils {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(StringUtils.class);

    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static String req2resp(String key) {
        if (key == null || "".equals(key.trim()) || key.length() <= 3) {
            return null;
        }
        if (key.endsWith("Req")) {
            key = key.substring(0, key.lastIndexOf("Req"));
            return key + "Resp";
        } else {
            return null;
        }
    }

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static void main(String[] args) {
        String mac = "080028593212";
        String time = new Date().toString();
        String key = "vs.touchcell.cn";
        System.out.println(MD5(mac + time + key));

        System.out.println(MD5("admin"));
    }

}
