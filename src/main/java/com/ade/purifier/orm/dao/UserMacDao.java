package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.model.UserMac;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * Created by ismeade on 2014/8/23.
 */
public class UserMacDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public UserMacDao(ObjectContext context) {
        this.context = context;
    }

    public List<UserMac> getUserMacByUserId(int UserId) {
        Expression expression = UserMac.USER_ID.eq(UserId);
        SelectQuery<UserMac> select = SelectQuery.query(UserMac.class, expression);
        List<UserMac> userMacs = context.select(select);
        return userMacs;
    }

    public List<UserMac> getUserMacByMacId(int macId) {
        Expression expression = UserMac.MAC_ID.eq(macId);
        SelectQuery<UserMac> select = SelectQuery.query(UserMac.class, expression);
        List<UserMac> userMacs = context.select(select);
        return userMacs;
    }

    public UserMac getUserMacByMacId(int UserId, int macId) {
        Expression expression = UserMac.MAC_ID.eq(macId);
        expression = expression.andExp(UserMac.USER_ID.eq(UserId));
        SelectQuery<UserMac> select = SelectQuery.query(UserMac.class, expression);
        List<UserMac> userMacs = context.select(select);
        return userMacs.size() > 0 ? userMacs.get(0) : null;
    }

    public int createUserMac(int userId, int macId) {
        UserMac userMac = this.context.newObject(UserMac.class);
        userMac.setUserId(userId);
        userMac.setMacId(macId);
        try {
            this.context.commitChanges();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return -1;
        }
        return userMac.getId();
    }

    public static void main(String[] args) {
        UserMacDao umDao = new UserMacDao(ObjectContextFactory.createObjectContext());
        umDao.createUserMac(204, 240);
    }

}
