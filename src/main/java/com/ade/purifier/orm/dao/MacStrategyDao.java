package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.model.MacStrategy;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ismeade on 2014/8/24.
 */
public class MacStrategyDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public MacStrategyDao(ObjectContext context) {
        this.context = context;
    }

    public MacStrategy getMacStrategy(int macId) {
        Expression expression = MacStrategy.MAC_ID.eq(macId);
        SelectQuery<MacStrategy> select = SelectQuery.query(MacStrategy.class, expression);
        List<MacStrategy> userMacs = context.select(select);
        if (userMacs.size() > 0) {
            if (userMacs.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: macId = " + macId);
            }
            return userMacs.get(0);
        } else {
            return null;
        }
    }

    public int createMacStrategy(int macId, int standard, String sleepBTime, String sleepETime, String outBTime, String outETime) {
        MacStrategy macStrategy = context.newObject(MacStrategy.class);
        macStrategy.setMacId(macId);
        macStrategy.setStandard(standard);
        macStrategy.setSleepBtime(sleepBTime);
        macStrategy.setSleepEtime(sleepETime);
        macStrategy.setOutBtime(outBTime);
        macStrategy.setOutEtime(outETime);
        macStrategy.setUpdateTime(new Date());
        try {
            context.commitChanges();
        } catch (Exception e) {
            logger.error("create:" + macStrategy + " fail.", e);
        }
        return macStrategy.getId();
    }

    public MacStrategy updateMacStrategy(int macId, int standard, String sleepBTime, String sleepETime, String outBTime, String outETime) {
        MacStrategy macStrategy = getMacStrategy(macId);
        if (macStrategy == null) {
            logger.error("no has MacStrategy : macId = " + macId);
            return null;
        }
        macStrategy.setStandard(standard);
        macStrategy.setSleepBtime(sleepBTime);
        macStrategy.setSleepEtime(sleepETime);
        macStrategy.setOutBtime(outBTime);
        macStrategy.setOutEtime(outETime);
        macStrategy.setUpdateTime(new Date());
        try {
            context.commitChanges();
        } catch (Exception e) {
            logger.error("update:" + macStrategy + " fail.", e);
            return null;
        }
        return macStrategy;
    }


}
