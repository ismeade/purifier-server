package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.model.UserInfo;
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
 * Created by ismeade on 2014/8/23.
 */
public class UserInfoDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public UserInfoDao(ObjectContext context) {
        this.context = context;
    }

    public List<UserInfo> getUserInfoAll() {
        SelectQuery<UserInfo> select = new SelectQuery(UserInfo.class);
        List<UserInfo> userInfos = context.performQuery(select);
        return userInfos;
    }

    public UserInfo getMacInfoById(int id) {
        UserInfo userInfo = (UserInfo) Cayenne.objectForPK(context, UserInfo.class, id);
        return userInfo;
    }

    public UserInfo getUserInfoByMobile(String mobile) {
        Expression expression = UserInfo.MOBILE.eq(mobile);
        SelectQuery<UserInfo> select = SelectQuery.query(UserInfo.class, expression);
        List<UserInfo> userInfos = context.select(select);
        if (userInfos.size() > 0) {
            if (userInfos.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: mobile = " + mobile);
            }
            return userInfos.get(0);
        } else {
            return null;
        }
    }

    public UserInfo getUserInfo(String mobile, String password) {
        Expression expression = UserInfo.MOBILE.eq(mobile);
        expression = expression.andExp(UserInfo.PASSWORD.eq(password));
        SelectQuery<UserInfo> select = SelectQuery.query(UserInfo.class, expression);
        List<UserInfo> userInfos = context.select(select);
        if (userInfos.size() > 0) {
            if (userInfos.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: mobile = " + mobile);
            }
            return userInfos.get(0);
        } else {
            return null;
        }
    }

    public UserInfo getUserInfoByEmail(String email) {
        Expression expression = UserInfo.EMAIL.eq(email);
        SelectQuery<UserInfo> select = SelectQuery.query(UserInfo.class, expression);
        List<UserInfo> userInfos = context.select(select);
        if (userInfos.size() > 0) {
            if (userInfos.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: email = " + email);
            }
            return userInfos.get(0);
        } else {
            return null;
        }
    }

    public UserInfo getUserInfoByMobileAndEmail(String mobile, String email) {
        Expression expression = UserInfo.MOBILE.eq(mobile);
        expression = expression.andExp(UserInfo.EMAIL.eq(email));
        SelectQuery<UserInfo> select = SelectQuery.query(UserInfo.class, expression);
        List<UserInfo> userInfos = context.select(select);
        if (userInfos.size() > 0) {
            if (userInfos.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: email = " + email);
            }
            return userInfos.get(0);
        } else {
            return null;
        }
    }

    public int createUserInfo(String mobile, String password, String email) {
        UserInfo userInfo = context.newObject(UserInfo.class);
        userInfo.setMobile(mobile);
        userInfo.setPassword(password);
        userInfo.setEmail(email);
        userInfo.setUpdateTime(new Date());
        try {
            context.commitChanges();
            return userInfo.getId();
        } catch (Exception e) {
            logger.error("create:" + userInfo + " fail.", e);
            return -1;
        }

    }



    public UserInfo updateUserInfo(UserInfo userInfo, String newPassword) {
        if (userInfo == null) {
            return null;
        }
        userInfo.setPassword(newPassword);
        userInfo.setUpdateTime(new Date());
        try {
            context.commitChanges();
            return userInfo;
        } catch (Exception e) {
//            context.rollbackChanges();
            logger.error("update:" + userInfo + " fail.", e);
            return null;
        }
    }

    public UserInfo updateUserInfo(String mobile, String newPassword, String newEmail) {
        UserInfo userInfo = this.getUserInfoByMobile(mobile);
        if (userInfo == null) {
            logger.error("no has UserInfo : mobile = " + mobile);
            return null;
        }
        userInfo.setPassword(newPassword);
        userInfo.setEmail(newEmail);
        userInfo.setUpdateTime(new Date());
        try {
            context.commitChanges();
            return userInfo;
        } catch (Exception e) {
//            context.rollbackChanges();
            logger.error("update:" + userInfo + " fail.", e);
            return null;
        }
    }

    public static void main(String[] args) {
        UserInfoDao uiDao = new UserInfoDao(ObjectContextFactory.createObjectContext());
        String mobile = "13911224009";
        String password = "15801603959";
        String passwordMd5 = StringUtils.MD5(mobile + password);
//        uiDao.createUserInfo("13911224009", "11111111111", "11111@111.com");
        UserInfo ui = uiDao.getUserInfoByMobile(mobile);
        uiDao.updateUserInfo(ui, passwordMd5);
    }

}
