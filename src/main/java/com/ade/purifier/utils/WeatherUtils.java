package com.ade.purifier.utils;

import com.ade.purifier.orm.dao.MacInfoDao;
import com.ade.purifier.orm.dao.WeatherAirDao;
import com.ade.purifier.orm.dao.WeatherInfoDao;
import com.ade.purifier.orm.model.MacInfo;
import com.ade.purifier.orm.model.WeatherAir;
import com.ade.purifier.orm.model.WeatherInfo;
import com.ade.purifier.orm.ObjectContextFactory;
import org.apache.cayenne.ObjectContext;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by ismeade on 2014/9/22.
 */
public class WeatherUtils {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(WeatherUtils.class);

    private static ObjectContext context = ObjectContextFactory.createObjectContext();
    private static WeatherAirDao weatherAirDao = new WeatherAirDao(context);
    private static WeatherInfoDao weatherInfoDao = new WeatherInfoDao(context);
    private static MacInfoDao macInfoDao = new MacInfoDao(context);

    private final static String HTTP_WEATHER_AIR_HEAD = "http://www.pm25.in/api/querys/pm2_5.json?city=";
    private final static String HTTP_WEATHER_AIR_END = "&token=LV2V7wCmYkiPh2Xzgd6q";
    private final static String HTTP_WEATHER_INFO_1_HEAD = "http://www.weather.com.cn/data/sk/";
    private final static String HTTP_WEATHER_INFO_2_HEAD = "http://www.weather.com.cn/data/cityinfo/";
    private final static String HTTP_WEATHER_INFO_END = ".html";

    public static byte[] getWeather(String mac) {
        MacInfo macInfo = macInfoDao.getMacInfoByMd5(mac);
        if (macInfo == null) {
            logger.error("没有找到净化器.mac:" + mac);
            return null;
        }
        String cityId = macInfo.getCityId();
        String city = macInfo.getCity();
        String area = macInfo.getArea();
        if (cityId == null || cityId.trim().equals("") || city == null || city.trim().equals("") || area == null || area.trim().equals("")) {
            logger.error("cityId, city or area is null.");
            return null;
        }
        byte[] w1 = getWeatherInfo(area, cityId);
        byte[] w2 = getWeatherAir(city);
        byte[] re = new byte[6];
        re[0] = w1[0];
        re[1] = w1[1];
        re[2] = (byte)(w2[2] * 16 + w1[2]);
        re[3] = w2[3];
        re[4] = w2[0];
        re[5] = w2[1];
        return re;
    }

    private static byte[] getWeatherInfo(String city, String cityId) {
        int temp2 = 0;
        int temp1 = 0;
        String weather = "";
        int umbrella = 0;

        WeatherInfo weatherInfo = weatherInfoDao.getWeatherInfoByCityId(cityId);
        if (weatherInfo == null || System.currentTimeMillis() - weatherInfo.getTime().getTime() > (1000 * 60 * 30)) {
            try {
                String wi1 = HttpUtils.http(HTTP_WEATHER_INFO_2_HEAD + cityId + HTTP_WEATHER_INFO_END);
                logger.info("天气情况报告" + wi1);
                Map<String, Object> wi1Map = (Map<String, Object>) Json.fromJson(wi1);
                Object data = wi1Map.get("weatherinfo");
                Map<String, Object> wi1dataMap = (Map<String, Object>)data;
                String _temp2 = (String) wi1dataMap.get("temp2");
                _temp2 = _temp2.substring(0, _temp2.length() - 1);
                temp2 = Integer.parseInt(_temp2);
                String _temp1 = (String) wi1dataMap.get("temp1");
                _temp1 = _temp1.substring(0, _temp1.length() - 1);
                temp1 = Integer.parseInt(_temp1);
                weather = (String) wi1dataMap.get("weather");
                if (weatherInfo == null) {
                    weatherInfoDao.insertWeatherInfo(city, cityId, "", weather, "", temp1, temp2, 0, new Date());
                } else {
                    weatherInfoDao.updateWeatherInfoByCityId(cityId, "", weather, "", temp1, temp2, 0, new Date());
                }

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        } else {
            temp2 = weatherInfo.getTemp2();
            temp1 = weatherInfo.getTemp1();
            weather = weatherInfo.getWeather();
        }
        if (weather.indexOf("雨") > 0) {
            umbrella = 1;
        }
        byte[] re = {(byte)temp2, (byte)temp1, (byte)umbrella};
        return re;
    }

    private static byte[] getWeatherAir(String city) {
        int pm25 = 0;
        int windos = 0;
        int led = 0;
        WeatherAir weatherAir = weatherAirDao.getWeatherAir(city);
        if (weatherAir == null || System.currentTimeMillis() - weatherAir.getTime().getTime() > (1000 * 60 * 30)) {
            try {
                String wi2 = HttpUtils.http(HTTP_WEATHER_AIR_HEAD + city + HTTP_WEATHER_AIR_END);
                logger.info("空气情况报告" + wi2);
                List<Object> list = (List) Json.fromJson(wi2);
                if (list.size() > 0) {
                    Map<String, Object> wi2Map = (Map<String, Object>) list.get(0);
                    pm25 = (Integer) wi2Map.get("aqi");
                }
                if (weatherAir == null) {
                    weatherAirDao.insertWeatherAir(city, pm25, new Date());
                } else {
                    weatherAirDao.updateWeatherAir(city, pm25, new Date());
                }

            } catch (Exception e) {
                logger.error("获取空气报告次数达到上限。");
                if (weatherAir != null) {
                    pm25 = weatherAir.getPm();
                }
            }
        } else {
            pm25 = weatherAir.getPm();
        }
        windos = pm25 <= 35 ? 1 : 0;
        led = pm25 >= 75 ? 1 : 0;
        byte[] _pm25 = ByteUtils.makeByte2(pm25);
        byte[] re = {_pm25[0], _pm25[1], (byte)windos, (byte)led};
        return re;
    }

    public static void main(String[] args) {
        System.out.println(ByteUtils.toHex(getWeather("080028593664")));
    }

}
