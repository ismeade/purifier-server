package com.ade.purifier.server.orm.model.auto;

import java.util.Date;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _WeatherAir was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _WeatherAir extends CayenneDataObject {

    @Deprecated
    public static final String CITY_PROPERTY = "city";
    @Deprecated
    public static final String PM_PROPERTY = "pm";
    @Deprecated
    public static final String TIME_PROPERTY = "time";

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> CITY = new Property<String>("city");
    public static final Property<Integer> PM = new Property<Integer>("pm");
    public static final Property<Date> TIME = new Property<Date>("time");

    public void setCity(String city) {
        writeProperty("city", city);
    }
    public String getCity() {
        return (String)readProperty("city");
    }

    public void setPm(Integer pm) {
        writeProperty("pm", pm);
    }
    public Integer getPm() {
        return (Integer)readProperty("pm");
    }

    public void setTime(Date time) {
        writeProperty("time", time);
    }
    public Date getTime() {
        return (Date)readProperty("time");
    }

}
