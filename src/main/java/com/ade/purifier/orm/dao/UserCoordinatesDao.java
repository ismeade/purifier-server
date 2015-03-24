package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.model.UserCoordinates;
import com.ade.purifier.orm.model.UserInfo;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ismeade on 2014/9/9.
 */
public class UserCoordinatesDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public UserCoordinatesDao(ObjectContext context) {
        this.context = context;
    }

    public UserCoordinates getUserCoordinates(int userId) {
        Expression expression = UserCoordinates.USER_ID.eq(userId);
        SelectQuery<UserCoordinates> query = SelectQuery.query(UserCoordinates.class, expression);
        List<UserCoordinates> list = context.performQuery(query);
        if (list.size() > 0) {
            if (list.size() > 1) {
                logger.info("发现多条数据，这不符合表的设计模式，请检查: userId = " + userId);
            }
            return list.get(0);
        } else {
            return null;
        }
    }

    public int insertUserCoordinates(int userId, String city, String cityId, String area, double longitude, double latitude) {
        UserCoordinates userCoordinates = context.newObject(UserCoordinates.class);
        userCoordinates.setUserId(userId);
        userCoordinates.setCity(city);
        userCoordinates.setCityId(cityId);
        userCoordinates.setArea(area);
        userCoordinates.setLongitude(longitude);
        userCoordinates.setLatitude(latitude);
        userCoordinates.setUpdateTime(new Date());
        try {
            context.commitChanges();
            return userCoordinates.getId();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return -1;
    }

    public int insertUserCoordinates(UserInfo user, String city, String cityId, String area, double longitude, double latitude) {
        int userId = user.getId();
        return insertUserCoordinates(userId, city, cityId, area, longitude, latitude);
    }

    public int updateUserCoordinates(int userId, String city, String cityId, String area, double longitude, double latitude) {
        UserCoordinates userCoordinates = getUserCoordinates(userId);
        userCoordinates.setCity(city);
        userCoordinates.setCityId(cityId);
        userCoordinates.setArea(area);
        userCoordinates.setLongitude(longitude);
        userCoordinates.setLatitude(latitude);
        userCoordinates.setUpdateTime(new Date());
        try {
            context.commitChanges();
            return userCoordinates.getId();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return -1;
    }

    public int updateUserCoordinates(UserInfo user, String city, String cityId, String area, double longitude, double latitude) {
        int userId = user.getId();
        return updateUserCoordinates(userId, city, cityId, area, longitude, latitude);
    }
}
