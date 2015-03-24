package com.ade.purifier.server.orm.model.auto;

import java.util.Date;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _UserCoordinates was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _UserCoordinates extends CayenneDataObject {

    @Deprecated
    public static final String AREA_PROPERTY = "area";
    @Deprecated
    public static final String CITY_PROPERTY = "city";
    @Deprecated
    public static final String CITY_ID_PROPERTY = "cityId";
    @Deprecated
    public static final String LATITUDE_PROPERTY = "latitude";
    @Deprecated
    public static final String LONGITUDE_PROPERTY = "longitude";
    @Deprecated
    public static final String UPDATE_TIME_PROPERTY = "updateTime";
    @Deprecated
    public static final String USER_ID_PROPERTY = "userId";

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> AREA = new Property<String>("area");
    public static final Property<String> CITY = new Property<String>("city");
    public static final Property<String> CITY_ID = new Property<String>("cityId");
    public static final Property<Double> LATITUDE = new Property<Double>("latitude");
    public static final Property<Double> LONGITUDE = new Property<Double>("longitude");
    public static final Property<Date> UPDATE_TIME = new Property<Date>("updateTime");
    public static final Property<Integer> USER_ID = new Property<Integer>("userId");

    public void setArea(String area) {
        writeProperty("area", area);
    }
    public String getArea() {
        return (String)readProperty("area");
    }

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

    public void setLatitude(Double latitude) {
        writeProperty("latitude", latitude);
    }
    public Double getLatitude() {
        return (Double)readProperty("latitude");
    }

    public void setLongitude(Double longitude) {
        writeProperty("longitude", longitude);
    }
    public Double getLongitude() {
        return (Double)readProperty("longitude");
    }

    public void setUpdateTime(Date updateTime) {
        writeProperty("updateTime", updateTime);
    }
    public Date getUpdateTime() {
        return (Date)readProperty("updateTime");
    }

    public void setUserId(Integer userId) {
        writeProperty("userId", userId);
    }
    public Integer getUserId() {
        return (Integer)readProperty("userId");
    }

}
