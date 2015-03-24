package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._MacStrategy;

public class MacStrategy extends _MacStrategy {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
