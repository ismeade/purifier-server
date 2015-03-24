package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._WeatherAir;

public class WeatherAir extends _WeatherAir {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
