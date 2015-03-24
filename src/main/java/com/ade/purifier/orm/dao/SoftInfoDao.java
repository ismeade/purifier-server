package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.model.SoftInfo;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ismeade on 2014/11/15.
 */
public class SoftInfoDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public SoftInfoDao(ObjectContext context) {
        this.context = context;
    }

    public int createSoftInfo(String appVersion, String fileName, String fileUrl, String os, String osVersion, int size, String md5, Date createTime, boolean senior) {
        SoftInfo softInfo = context.newObject(SoftInfo.class);
        softInfo.setAppVersion(appVersion);
        softInfo.setFileName(fileName);
        softInfo.setFileUrl(fileUrl);
        softInfo.setOs(os);
        softInfo.setOsVersion(osVersion);
        softInfo.setSize(size);
        softInfo.setMd5(md5);
        softInfo.setCreateTime(createTime);
        softInfo.setIsSenior(senior);
        try {
            context.commitChanges();
            logger.info("insert success:" + softInfo);
            return softInfo.getId();
        } catch (Exception e) {
            logger.error("insert error:" + e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public List<SoftInfo> getSoftInfoByOs(String os, String osVersion) {
        Expression expression = SoftInfo.OS.eq(os);
        expression = expression.andExp(SoftInfo.OS_VERSION.eq(osVersion));
        SelectQuery<SoftInfo> select = SelectQuery.query(SoftInfo.class, expression);
        try {
            List<SoftInfo> list = context.select(select);
            logger.info("select success:" + list);
            return list;
        } catch (Exception e) {
            logger.error("select error:" + e.getLocalizedMessage(), e);
            return new ArrayList<>();
        }
    }

    public SoftInfo getSoftInfoByOsSenior(String os, String osVersion, boolean senior) {
        Expression expression = SoftInfo.OS.eq(os);
        expression = expression.andExp(SoftInfo.OS_VERSION.eq(osVersion));
        expression = expression.andExp(SoftInfo.IS_SENIOR.eq(senior));
        SelectQuery<SoftInfo> select = SelectQuery.query(SoftInfo.class, expression);
        try {
            List<SoftInfo> list = context.select(select);
            logger.info("select success:" + list);
            return list.size() == 1 ? list.get(0) : null;
        } catch (Exception e) {
            logger.error("select error:" + e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) {
        SoftInfoDao softInfoDao = new SoftInfoDao(ObjectContextFactory.createObjectContext());
        softInfoDao.createSoftInfo("android-1.0-bate", "android-1.0-bate.apk", "http://gdown.baidu.com/data/wisegame/8f310632864d3729/anzhuoshichang_84.apk", "andorid", "17", 5290, "b2938f8b88f47bdb4a4ad2b3e87049d8", new Date(), true);
    }

}
