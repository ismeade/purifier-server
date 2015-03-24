package com.ade.purifier.server.orm.model.auto;

import java.util.Date;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _WeatherInfo was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _WeatherInfo extends CayenneDataObject {

    @Deprecated
    public static final String CITY_PROPERTY = "city";
    @Deprecated
    public static final String CITY_ID_PROPERTY = "cityId";
    @Deprecated
    public static final String SD_PROPERTY = "sd";
    @Deprecated
    public static final String TEMP1_PROPERTY = "temp1";
    @Deprecated
    public static final String TEMP2_PROPERTY = "temp2";
    @Deprecated
    public static final String TIME_PROPERTY = "time";
    @Deprecated
    public static final String WD_PROPERTY = "wd";
    @Deprecated
    public static final String WEATHER_PROPERTY = "weather";
    @Deprecated
    public static final String WS_PROPERTY = "ws";

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> CITY = new Property<String>("city");
    public static final Property<String> CITY_ID = new Property<String>("cityId");
    public static final Property<Integer> SD = new Property<Integer>("sd");
    public static final Property<Integer> TEMP1 = new Property<Integer>("temp1");
    public static final Property<Integer> TEMP2 = new Property<Integer>("temp2");
    public static final Property<Date> TIME = new Property<Date>("time");
    public static final Property<String> WD = new Property<String>("wd");
    public static final Property<String> WEATHER = new Property<String>("weather");
    public static final Property<String> WS = new Property<String>("ws");

    public void setCity(String city) {
        writeProperty("city", city);
    }
    public String getCity() {
        return (String)readProperty("city");
    }

    public void setCityId(String cityId) {
        writeProperty("cityId", cityId);
    }
    public String getCityId() {
        return (String)readProperty("cityId");
    }

    public void setSd(Integer sd) {
        writeProperty("sd", sd);
    }
    public Integer getSd() {
        return (Integer)readProperty("sd");
    }

    public void setTemp1(Integer temp1) {
        writeProperty("temp1", temp1);
    }
    public Integer getTemp1() {
        return (Integer)readProperty("temp1");
    }

    public void setTemp2(Integer temp2) {
        writeProperty("temp2", temp2);
    }
    public Integer getTemp2() {
        return (Integer)readProperty("temp2");
    }

    public void setTime(Date time) {
        writeProperty("time", time);
    }
    public Date getTime() {
        return (Date)readProperty("time");
    }

    public void setWd(String wd) {
        writeProperty("wd", wd);
    }
    public String getWd() {
        return (String)readProperty("wd");
    }

    public void setWeather(String weather) {
        writeProperty("weather", weather);
    }
    public String getWeather() {
        return (String)readProperty("weather");
    }

    public void setWs(String ws) {
        writeProperty("ws", ws);
    }
    public String getWs() {
        return (String)readProperty("ws");
    }

}
