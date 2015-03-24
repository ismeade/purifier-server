package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._WeatherInfo;

public class WeatherInfo extends _WeatherInfo {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
