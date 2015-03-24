package com.ade.purifier.orm.dao;

import com.ade.purifier.orm.model.WeatherAir;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by ismeade on 2014/9/22.
 */
public class WeatherAirDao {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private ObjectContext context;

    public WeatherAirDao(ObjectContext context) {
        this.context = context;
    }

    public WeatherAir getWeatherAir(String city) {
        Expression expression = WeatherAir.CITY.eq(city);
        SelectQuery<WeatherAir> select = SelectQuery.query(WeatherAir.class, expression);
        List<WeatherAir> list = context.select(select);
        if (list.size() > 1) {
            logger.error("发现多条结果，这不符合数据库设计模式.city:" + city);
        }
        return list.size() > 0 ? list.get(0) : null;
    }

    public int insertWeatherAir(String city, int pm, Date time) {
        WeatherAir weatherAir = context.newObject(WeatherAir.class);
        weatherAir.setCity(city);
        weatherAir.setPm(pm);
        weatherAir.setTime(time);
        try {
            context.commitChanges();
            return weatherAir.getId();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public WeatherAir updateWeatherAir(String city, int pm, Date time) {
        WeatherAir weatherAir = getWeatherAir(city);
        if (weatherAir != null) {
            weatherAir.setPm(pm);
            weatherAir.setTime(time);
            try {
                context.commitChanges();
                return weatherAir;
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                return null;
            }
        } else {
            logger.error("空气质量记录。city:" + city);
            return null;
        }
    }

}
