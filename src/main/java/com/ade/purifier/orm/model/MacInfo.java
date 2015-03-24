package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._MacInfo;

public class MacInfo extends _MacInfo {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
