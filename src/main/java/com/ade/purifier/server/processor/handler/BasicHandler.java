package com.ade.purifier.server.processor.handler;

import org.apache.cayenne.ObjectContext;

import java.util.Map;

/**
 * Created by ismeade on 2014/8/30.
 */
public interface BasicHandler {

    public Map<String, Object> work(String mobile, Object obj, ObjectContext context);

}