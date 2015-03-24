package com.ade.purifier.server.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ismeade on 2014/12/31.
 */
public class SMSProperties {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(SMSProperties.class);

    private final static String FILE_NAME = "/sms.properties";

    private final static String KEY_URL = "url";
    private final static String KEY_UN  = "un";
    private final static String KEY_PWD = "pwd";
    private final static String KEY_TIMEOUT = "timeout";

    static String VALUE_URL;
    static String VALUE_UN;
    static String VALUE_PWD;
    static int VALUE_TIMEOUT;

    static {
        initialize();
    }

    private static void initialize() {
        logger.info("load file: " + FILE_NAME);
        InputStream in = null;
        try {
            in = SMSProperties.class.getResourceAsStream(FILE_NAME);
            Properties properties = new Properties();
            properties.load(in);
            VALUE_URL = properties.getProperty(KEY_URL, "");
            VALUE_UN  = properties.getProperty(KEY_UN, "");
            VALUE_PWD = properties.getProperty(KEY_PWD, "");
            VALUE_TIMEOUT = Integer.parseInt(properties.getProperty(KEY_TIMEOUT, "1800"));
        } catch (FileNotFoundException e) {
            logger.error(e.getLocalizedMessage() + " 没有找到配置文件:" + FILE_NAME);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(SMSProperties.VALUE_URL);
        System.out.println(SMSProperties.VALUE_UN);
        System.out.println(SMSProperties.VALUE_PWD);
        System.out.println(SMSProperties.VALUE_TIMEOUT);
    }

}
