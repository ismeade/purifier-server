package com.ade.purifier.orm;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;

/**
 *
 * Created by ismeade on 2014/8/30.
 */
public class ObjectContextFactory {

    public static final String CAYENNE_FILE = "cayenne-purifier-server.xml";

    private static ServerRuntime cayenneRuntime = new ServerRuntime(CAYENNE_FILE);

    public static ObjectContext createObjectContext() {
        return cayenneRuntime.newContext();
    }

}
