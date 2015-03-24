package com.ade.purifier.orm.model;

import com.ade.purifier.server.orm.model.auto._SoftInfo;

public class SoftInfo extends _SoftInfo {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
