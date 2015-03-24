package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.dao.MacInfoDao;
import com.ade.purifier.orm.model.MacInfo;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ismeade on 2014/11/13.
 */
public class AcpLoginHandler {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(AcpLoginHandler.class);

    private static ObjectContext context = ObjectContextFactory.createObjectContext();

    private static MacInfoDao macInfoDao = new MacInfoDao(context);

    public static String getMd5ByMac(String mac) {
        if (mac == null || mac.equals("")) {
            logger.error("mac is null or \'\'");
            return null;
        }
        MacInfo macInfo = macInfoDao.getMacInfoByMac(mac);
        if (macInfo != null) {
            return macInfo.getMd5();
        } else {
            return null;
        }
    }


}
