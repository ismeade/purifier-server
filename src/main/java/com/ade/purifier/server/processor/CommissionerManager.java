package com.ade.purifier.server.processor;

import com.ade.purifier.orm.ObjectContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ismeade on 2014/8/30.
 */
public class CommissionerManager {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(CommissionerManager.class);

    private static ConcurrentMap<String, Commissioner> map = new ConcurrentHashMap<>();

    public static Commissioner getCommissioner(String mobile) {
        if (mobile == null || "".equals(mobile.trim())) {
            logger.error("mobile is null or \"\"");
            return null;
        }
        Commissioner commissioner = map.get(mobile);
        if (commissioner == null) {
            commissioner = newCommissioner(mobile);
        }
        return commissioner;
    }

    private static Commissioner newCommissioner(String mobile) {
        Commissioner commissioner = new Commissioner(mobile, ObjectContextFactory.createObjectContext());
        logger.info("create a new Commissioner:" + commissioner);
        map.put(mobile, commissioner);
        return commissioner;
    }

}
