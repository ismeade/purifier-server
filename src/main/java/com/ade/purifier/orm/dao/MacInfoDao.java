package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.model.MacInfo;
import com.ade.purifier.utils.StringUtils;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by ismeade on 2014/8/23.
 */
public class MacInfoDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public MacInfoDao(ObjectContext context) {
        this.context = context;
    }

    public List<MacInfo> getMacInfoAll() {
        SelectQuery<MacInfo> select = new SelectQuery<MacInfo>(MacInfo.class);
        return context.performQuery(select);
    }

    public MacInfo getMacInfoById(int id) {
        MacInfo macInfo = Cayenne.objectForPK(context, MacInfo.class, id);
        return macInfo;
    }

    public MacInfo getMacInfoByMd5(String md5) {
        Expression expression = MacInfo.MD5.eq(md5);
        SelectQuery<MacInfo> select = SelectQuery.query(MacInfo.class, expression);
        List<MacInfo> macInfos = context.select(select);
        if (macInfos.size() > 0) {
            if (macInfos.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: md5 = " + md5);
            }
            return macInfos.get(0);
        } else {
            return null;
        }
    }

    public MacInfo getMacInfoByMac(String mac) {
        Expression expression = MacInfo.MAC.eq(mac);
        SelectQuery<MacInfo> select = SelectQuery.query(MacInfo.class, expression);
        List<MacInfo> macInfos = context.select(select);
        if (macInfos.size() > 0) {
            if (macInfos.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: md5 = " + mac);
            }
            return macInfos.get(0);
        } else {
            return null;
        }
    }

    public int createMacInfo(String mac, String name, double longitude, double latitude) {
        MacInfo macInfo = context.newObject(MacInfo.class);
        macInfo.setMac(mac);
        macInfo.setName(name);
        macInfo.setLongitude(longitude);
        macInfo.setLatitude(latitude);
        try {
            context.commitChanges();
        } catch (Exception e) {
            logger.error("create:" + macInfo + " fail.", e);
        }
        return macInfo.getId();
    }

    public int createMacInfo(String mac, String name) {
        return createMacInfo(mac, name, 0, 0);
    }

    public int registereMacInfo(String mac, String md5) {
        if (getMacInfoByMac(mac) != null) {
            return -5;
        }
        MacInfo macInfo = context.newObject(MacInfo.class);
        macInfo.setMac(mac);
        macInfo.setMd5(md5);
        try {
            context.commitChanges();
        } catch (Exception e) {
            logger.error("create:" + macInfo + " fail.", e);
        }
        return macInfo.getId();
    }

    public MacInfo updateMacInfo(int macId, String city, String cityId, String area, double longitude, double latitude) {
        MacInfo macInfo = getMacInfoById(macId);
        if (macInfo == null) {
            logger.error("no has MacInfo : mac = " + macId);
            return null;
        }
        macInfo.setCity(city);
        macInfo.setCityId(cityId);
        macInfo.setArea(area);
        macInfo.setLongitude(longitude);
        macInfo.setLatitude(latitude);
        try {
            context.commitChanges();
            return macInfo;
        } catch (Exception e) {
            logger.error("update:" + macInfo + " fail.", e);
            return null;
        }
    }

    public MacInfo updateMacInfo(String mac, String city, String cityId, String area, double longitude, double latitude) {
        MacInfo macInfo = getMacInfoByMd5(mac);
        if (macInfo == null) {
            logger.error("no has MacInfo : mac = " + mac);
            return null;
        }
        macInfo.setCity(city);
        macInfo.setCityId(cityId);
        macInfo.setArea(area);
        macInfo.setLongitude(longitude);
        macInfo.setLatitude(latitude);
        try {
            context.commitChanges();
            return macInfo;
        } catch (Exception e) {
            logger.error("update:" + macInfo + " fail.", e);
            return null;
        }
    }

    public MacInfo updateMacInfoName(int macId, String name) {
        if (name == null || name.trim().equals("")) {
            return null;
        }
        MacInfo macInfo = getMacInfoById(macId);
        if (macInfo == null) {
            logger.error("no has MacInfo : mac = " + macId);
            return null;
        }
        macInfo.setName(name);
        try {
            context.commitChanges();
            return macInfo;
        } catch (Exception e) {
            logger.error("update:" + macInfo + " fail.", e);
            return null;
        }
    }

    public static void main(String[] args) {
        MacInfoDao m = new MacInfoDao(ObjectContextFactory.createObjectContext());
//        m.getMacInfoById(1);
        String mac = "08002859322c";
        String md5 = StringUtils.MD5(mac + new Date().toString() + "vs.touchcell.cn");
        m.registereMacInfo(mac, md5);
        System.out.println("mac:" + mac + " md5:" + md5);
    }

}
