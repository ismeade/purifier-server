package com.ade.purifier.server.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * Created by ismeade on 2014/12/18.
 */
public class WeatherCache {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(WeatherCache.class);

    private static ConcurrentMap<String, Weather> map = new ConcurrentHashMap<>();

    private static void init() {

    }

    private static void flash() {

    }

}
