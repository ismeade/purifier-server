package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._UserInfo;

public class UserInfo extends _UserInfo {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
