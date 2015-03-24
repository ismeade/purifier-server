package com.ade.purifier.utils;

/**
 * Created by ismeade on 2014/8/29.
 */
public class MessageUtils {

    public final static byte HEAD = (byte) 0x02;
    public final static byte END  = (byte) 0x03;

    public static boolean isFormat(byte[] message) {
        if (message == null || message.length <= 5) {
            return false;
        }
        if (message[0] != HEAD || message[message.length - 1] != END) {
            return false;
        }
        if (message[1] != message.length - 5) {
            return false;
        }
        return true;
    }

    public static byte[] getDatas(byte[] b) {
        byte[] re = new byte[b.length - 6];
        for (int i = 0; i < re.length; i++) {
            re[i] = b[i + 3];
        }
        return re;
    }



}
