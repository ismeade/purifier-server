package com.ade.purifier.utils;

import java.util.Random;

/**
 * Created by ismeade on 2015/1/5.
 */
public class RandomUtils {

    public static int random() {
        int max = 999999;
        int min = 100000;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    public static void main(String[] args) {
        random();
        random();
        random();
        random();
    }

}
