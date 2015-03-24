package com.ade.purifier.server.weather;

/**
 * Created by ismeade on 2014/12/18.
 */
public class WeatherWorker implements Runnable {

    private final static String PM_KEY = "LV2V7wCmYkiPh2Xzgd6q";

    private final static String PM_URL = "http://www.pm25.in/api/querys/aqi_ranking.json?token=" + PM_KEY;

    @Override
    public void run() {

    }

}
