package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._UserCoordinates;

public class UserCoordinates extends _UserCoordinates {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
