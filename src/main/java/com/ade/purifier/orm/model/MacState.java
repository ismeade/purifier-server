package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._MacState;

public class MacState extends _MacState {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
