package com.ade.purifier.server.processor.handler;

import com.ade.purifier.orm.dao.MacInfoDao;
import com.ade.purifier.orm.dao.UserCoordinatesDao;
import com.ade.purifier.orm.dao.UserInfoDao;
import com.ade.purifier.orm.dao.UserMacDao;
import com.ade.purifier.orm.model.MacInfo;
import com.ade.purifier.orm.model.UserCoordinates;
import com.ade.purifier.orm.model.UserInfo;
import com.ade.purifier.orm.model.UserMac;
import org.apache.cayenne.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置城市
 * Created by ismeade on 2014/9/9.
 */
public class SetCityHandler implements BasicHandler {

    private final Logger logger = (Logger) LoggerFactory.getLogger(getClass());

    private final static String KEY_CITY = "city";
    private final static String KEY_CITY_ID = "cityId";

    private final static String KEY_AREA = "area";
    private final static String KEY_LONGITUDE = "longitude";
    private final static String KEY_LATITUDE = "latitude";

    private final static String KEY_CODE = "code";
    private final static String KEY_MESSAGE = "message";

    @Override
    public Map<String, Object> work(String mobile, Object obj, ObjectContext context) {
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof Map<?, ?>) {
            UserInfoDao userService = new UserInfoDao(context);
            UserInfo user = userService.getUserInfoByMobile(mobile);
            if (user == null) {
                map.put(KEY_CODE, -2);
                map.put(KEY_MESSAGE, "用户还没注册.mobile:" + mobile);
                return map;
            }

            Map<String, Object> data = (Map<String, Object>) obj;
            // city
            Object _city = data.get(KEY_CITY);
            if (_city == null || !(_city instanceof String)) {
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "城市为空或不是String.");
                return map;
            }
            String city = (String) _city;
            Object _cityId = data.get(KEY_CITY_ID);
            if (_cityId == null || !(_cityId instanceof String)) {
                map.put(KEY_CODE, 2);
                map.put(KEY_MESSAGE, "城市号为空或不是int.");
                return map;
            }
            String cityId = (String) _cityId;
            // area
            Object _area = data.get(KEY_AREA);
            if (_area == null || !(_area instanceof String)) {
                map.put(KEY_CODE, 3);
                map.put(KEY_MESSAGE, "区号为空或不是String.");
                return map;
            }
            String area = (String) _area;
            // longitude
            Object _longitude = data.get(KEY_LONGITUDE);
            double longitude = 0;
            if (_longitude != null && _longitude instanceof Integer) {
                longitude = (Integer) _longitude;
            } else if (_longitude != null && _longitude instanceof Double) {
                longitude = (Double) _longitude;
            } else {
                map.put(KEY_CODE, 4);
                map.put(KEY_MESSAGE, "经度不是double或Integer类型");
                return map;
            }
            // latitude
            Object _latitude = data.get(KEY_LATITUDE);
            double latitude = 0;
            if (_latitude != null && _latitude instanceof Integer) {
                latitude = (Integer) _latitude;
            } else if (_latitude != null && _latitude instanceof Double) {
                latitude = (Double) _latitude;
            } else {
                map.put(KEY_CODE, 5);
                map.put(KEY_MESSAGE, "纬度不是double或Integer类型");
                return map;
            }
            UserCoordinatesDao userCoordinatesDao = new UserCoordinatesDao(context);
            MacInfoDao macInfoDao = new MacInfoDao(context);
            UserMacDao userMacDao = new UserMacDao(context);
            UserCoordinates userCoordinates = userCoordinatesDao.getUserCoordinates(user.getId());
            int id = -1;
            if (userCoordinates == null) {
                id = userCoordinatesDao.insertUserCoordinates(user, city, cityId, area, longitude, latitude);
            } else {
                id = userCoordinatesDao.updateUserCoordinates(user, city, cityId, area, longitude, latitude);
            }


            List<UserMac> list = userMacDao.getUserMacByUserId(user.getId());
            for (UserMac usermac : list) {
                MacInfo macInfo = macInfoDao.getMacInfoById(usermac.getMacId());
                if (macInfo == null) {

                } else {
                    if (macInfo.getCity() == null || macInfo.getCity().trim().equals("")){
                        macInfoDao.updateMacInfo(usermac.getMacId(), city, cityId, area, longitude, latitude);
                    }
                }

            }
            if (id > 0) {
                map.put(KEY_CODE, 0);
                map.put(KEY_MESSAGE, "设置用户城市信息成功.");
                return map;
            } else {
                map.put(KEY_CODE, -4);
                map.put(KEY_MESSAGE, "设置用户城市信息失败.");
                return map;
            }
        } else {
            map.put(KEY_CODE, -1);
            map.put(KEY_MESSAGE, "registerReq信息结构错误.");
            return map;
        }
    }
}