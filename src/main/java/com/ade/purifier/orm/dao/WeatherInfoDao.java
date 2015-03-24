package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.model.WeatherInfo;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by ismeade on 2014/9/22.
 */
public class WeatherInfoDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public WeatherInfoDao(ObjectContext context) {
        this.context = context;
    }

    public WeatherInfo getWeatherInfoByCity(String city) {
        Expression expression = WeatherInfo.CITY.eq(city);
        SelectQuery<WeatherInfo> select = SelectQuery.query(WeatherInfo.class, expression);
        List<WeatherInfo> userMacs = context.select(select);
        if (userMacs.size() > 1) {
            logger.error("发现多条结果，这不符合数据库设计模式.city:" + city);
        }
        return userMacs.size() > 0 ? userMacs.get(0) : null;
    }

    public WeatherInfo getWeatherInfoByCityId(String cityId) {
        Expression expression = WeatherInfo.CITY_ID.eq(cityId);
        SelectQuery<WeatherInfo> select = SelectQuery.query(WeatherInfo.class, expression);
        List<WeatherInfo> userMacs = context.select(select);
        if (userMacs.size() > 1) {
            logger.error("发现多条结果，这不符合数据库设计模式.cityId:" + cityId);
        }
        return userMacs.size() > 0 ? userMacs.get(0) : null;
    }

    public int insertWeatherInfo(String city, String cityId, String ws, String weather, String wd, int temp1, int temp2, int sd, Date time) {
        WeatherInfo weatherInfo = context.newObject(WeatherInfo.class);
        weatherInfo.setCity(city);
        weatherInfo.setCityId(cityId);
        weatherInfo.setWs(ws);
        weatherInfo.setWeather(weather);
        weatherInfo.setWd(wd);
        weatherInfo.setTemp1(temp1);
        weatherInfo.setTemp2(temp2);
        weatherInfo.setSd(sd);
        weatherInfo.setTime(time);
        try {
            context.commitChanges();
            return weatherInfo.getId();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public WeatherInfo updateWeatherInfoByCity(String city, String ws, String weather, String wd, int temp1, int temp2, int sd, Date time) {
        WeatherInfo weatherInfo = getWeatherInfoByCity(city);
        if (weatherInfo != null) {
            weatherInfo.setWs(ws);
            weatherInfo.setWeather(weather);
            weatherInfo.setWd(wd);
            weatherInfo.setTemp1(temp1);
            weatherInfo.setTemp2(temp2);
            weatherInfo.setSd(sd);
            weatherInfo.setTime(time);
            try {
                context.commitChanges();
                return weatherInfo;
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return null;
            }
        } else {
            logger.error("没有天气记录。city:" + city);
            return null;
        }
    }

    public WeatherInfo updateWeatherInfoByCityId(String cityId, String ws, String weather, String wd, int temp1, int temp2, int sd, Date time) {
        WeatherInfo weatherInfo = getWeatherInfoByCityId(cityId);
        if (weatherInfo != null) {
            weatherInfo.setWs(ws);
            weatherInfo.setWeather(weather);
            weatherInfo.setWd(wd);
            weatherInfo.setTemp1(temp1);
            weatherInfo.setTemp2(temp2);
            weatherInfo.setSd(sd);
            weatherInfo.setTime(time);
            try {
                context.commitChanges();
                return weatherInfo;
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return null;
            }
        } else {
            logger.error("没有天气记录。cityId:" + cityId);
            return null;
        }
    }

}
