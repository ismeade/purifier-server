package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.model.SysUser;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ismeade on 2014/11/14.
 */
public class SysUserDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public SysUserDao(ObjectContext context) {
        System.out.println("#######" + context);
        this.context = context;
    }

    public SysUser getSysUser(int id) {
        SysUser user = Cayenne.objectForPK(context, SysUser.class, id);
        return user;
    }

    public List<SysUser> getSysUser() {
        SelectQuery<SysUser> select = SelectQuery.query(SysUser.class);
        return context.performQuery(select);
    }

    public SysUser getSysUser(String userName) {
        Expression expression = SysUser.USER_NAME.eq(userName);
        SelectQuery<SysUser> select = SelectQuery.query(SysUser.class, expression);
        List<SysUser> sysUser = context.performQuery(select);
        return sysUser.size() > 0 ? sysUser.get(0) : null;
    }

    public SysUser getSysUser(String userName, String passowrd) {
        Expression expression = SysUser.USER_NAME.eq(userName);
        expression = expression.andExp(SysUser.PASSWORD.eq(passowrd));
        SelectQuery<SysUser> select = SelectQuery.query(SysUser.class, expression);
        List<SysUser> sysUser = context.performQuery(select);
        return sysUser.size() > 0 ? sysUser.get(0) : null;
    }

    public SysUser createSysUser(String userName, String passowrd, String email) {
        SysUser user = context.newObject(SysUser.class);
        user.setUserName(userName);
        user.setPassword(passowrd);
        user.setEmail(email);
        try {
            context.commitChanges();
            logger.info("create success:" + user);
            return user;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void deleteSysUser(int id) {
        SysUser user = getSysUser(id);
        context.deleteObjects(user);
    }



}
