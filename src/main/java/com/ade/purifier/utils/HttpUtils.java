package com.ade.purifier.utils;

import org.nutz.json.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by ismeade on 2014/9/22.
 */
public class HttpUtils {

    public static String http(String url) {
        try {
            URL u = new URL(url);
            URLConnection con = u.openConnection();
            BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            return r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Reader get(String url) {
        try {
            URL u = new URL(url);
            URLConnection con = u.openConnection();
            BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        String url = "http://www.pm25.in/api/querys/all_cities.json?token=LV2V7wCmYkiPh2Xzgd6q";
        String url = "http://www.pm25.in/api/querys/aqi_ranking.json?token=LV2V7wCmYkiPh2Xzgd6q";
        Reader r = get(url);
        Object obj = Json.fromJson(r);
        if (obj != null && obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            for (Object m : list) {
                if (m != null && m instanceof Map) {
                    Map<String, Object> a = (Map<String, Object>) m;
                    System.out.println(a);
                }
            }
            System.out.println(list.size());
        }

    }

}
