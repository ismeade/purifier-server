package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._UserMac;

public class UserMac extends _UserMac {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
