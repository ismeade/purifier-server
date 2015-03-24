package com.ade.purifier.server.orm.model.auto;

import java.util.Date;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _MacStateHis was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _MacStateHis extends CayenneDataObject {

    @Deprecated
    public static final String DATE_PROPERTY = "date";
    @Deprecated
    public static final String FINE_PM_PROPERTY = "finePm";
    @Deprecated
    public static final String HUMIDITY_PROPERTY = "humidity";
    @Deprecated
    public static final String MAC_ID_PROPERTY = "macId";
    @Deprecated
    public static final String OUT_BTIME_PROPERTY = "outBtime";
    @Deprecated
    public static final String OUT_ETIME_PROPERTY = "outEtime";
    @Deprecated
    public static final String SMELL_PROPERTY = "smell";
    @Deprecated
    public static final String TEMPERATURE_PROPERTY = "temperature";

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Date> DATE = new Property<Date>("date");
    public static final Property<Integer> FINE_PM = new Property<Integer>("finePm");
    public static final Property<Integer> HUMIDITY = new Property<Integer>("humidity");
    public static final Property<Integer> MAC_ID = new Property<Integer>("macId");
    public static final Property<String> OUT_BTIME = new Property<String>("outBtime");
    public static final Property<String> OUT_ETIME = new Property<String>("outEtime");
    public static final Property<Integer> SMELL = new Property<Integer>("smell");
    public static final Property<Integer> TEMPERATURE = new Property<Integer>("temperature");

    public void setDate(Date date) {
        writeProperty("date", date);
    }
    public Date getDate() {
        return (Date)readProperty("date");
    }

    public void setFinePm(Integer finePm) {
        writeProperty("finePm", finePm);
    }
    public Integer getFinePm() {
        return (Integer)readProperty("finePm");
    }

    public void setHumidity(Integer humidity) {
        writeProperty("humidity", humidity);
    }
    public Integer getHumidity() {
        return (Integer)readProperty("humidity");
    }

    public void setMacId(Integer macId) {
        writeProperty("macId", macId);
    }
    public Integer getMacId() {
        return (Integer)readProperty("macId");
    }

    public void setOutBtime(String outBtime) {
        writeProperty("outBtime", outBtime);
    }
    public String getOutBtime() {
        return (String)readProperty("outBtime");
    }

    public void setOutEtime(String outEtime) {
        writeProperty("outEtime", outEtime);
    }
    public String getOutEtime() {
        return (String)readProperty("outEtime");
    }

    public void setSmell(Integer smell) {
        writeProperty("smell", smell);
    }
    public Integer getSmell() {
        return (Integer)readProperty("smell");
    }

    public void setTemperature(Integer temperature) {
        writeProperty("temperature", temperature);
    }
    public Integer getTemperature() {
        return (Integer)readProperty("temperature");
    }

}
