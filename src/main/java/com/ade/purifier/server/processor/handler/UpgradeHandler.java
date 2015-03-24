package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.dao.SoftInfoDao;
import com.ade.purifier.orm.model.SoftInfo;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by ismeade on 2014/11/15.
 */
public class UpgradeHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_OS  = "os";
    private final static String KEY_OSVERSION  = "osVersion";

    private final static String KEY_APPVERSION = "appVersion";
    private final static String KEY_MD5        = "md5";
    private final static String KEY_SIZE       = "size";
    private final static String KEY_FILENAME   = "fileName";
    private final static String KEY_FILEURL    = "fileUrl";



    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            Map<String, Object> data = (Map<String, Object>) obj;
            Object _os = data.get(KEY_OS);
            if (_os == null || !(_os instanceof String) || "".equals(((String) _os).trim())) {
                logger.error("os is null or \'\'.");
                return null;
            }
            Object _osVersion = data.get(KEY_OSVERSION);
            if (_osVersion == null || !(_osVersion instanceof String) || "".equals(((String) _osVersion).trim())) {
                logger.error("osVersion is null or \'\'.");
                return null;
            }
            String os = (String) _os;
            String osVersion = (String) _osVersion;
            SoftInfoDao softInfoDao = new SoftInfoDao(context);
            SoftInfo softInfo = softInfoDao.getSoftInfoByOsSenior(os, osVersion, true);
            if (softInfo != null) {
                map.put(KEY_APPVERSION, softInfo.getAppVersion());
                map.put(KEY_MD5, softInfo.getMd5());
                map.put(KEY_SIZE, softInfo.getSize());
                map.put(KEY_FILENAME, softInfo.getFileName());
                map.put(KEY_FILEURL, softInfo.getFileUrl());
                return map;
            } else {
                logger.error("没有找到软件版本信息: os->" + os + " osVersion->" + osVersion);
                return null;
            }
        } else {
            logger.error("UpgradeReq信息结构错误.");
            return null;
        }
    }

}
