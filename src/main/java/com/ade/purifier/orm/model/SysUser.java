package com.ade.purifier.orm.model;

import com.ade.purifier.server.orm.model.auto._SysUser;

public class SysUser extends _SysUser {

    public int getId() {
        return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }

}
