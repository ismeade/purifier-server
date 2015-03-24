package com.ade.purifier.orm.model;

import com.ade.purifier.orm.model.auto._PurifierMap;

public class PurifierMap extends _PurifierMap {

    private static PurifierMap instance;

    private PurifierMap() {}

    public static PurifierMap getInstance() {
        if(instance == null) {
            instance = new PurifierMap();
        }

        return instance;
    }
}
