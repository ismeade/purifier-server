package com.ade.purifier.utils;

/**
 * Created by ismeade on 2014/9/21.
 *
 */
public class ByteUtils {

    public static final String toHex(byte hash[]) {
        if (hash == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if (((int) hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String toStr(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }

    public static String toStr(byte[] datas) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < datas.length; i++) {
            String hex = Integer.toHexString(datas[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            buffer.append(hex.toUpperCase());
        }
        return buffer.toString();
    }

    public static int makeUint8(byte b) {
        return (0xff & b);
    }

    public static int makeUint16(byte b, byte c) {
        return (0xff & b) << 8 | (0xff & c);
    }

    public static int makeUint32(byte b, byte c, byte d, byte e) {
        return (((0xff & b) << 24) | ((0xff & c) << 16) | ((0xff & d) << 8) | ((0xff & e) << 0));
    }

    public static byte[] makeByte4(int v) {
        byte[] buf = { (byte) ((v & 0xff000000) >> 32),
                (byte) ((v & 0xff0000) >> 16), (byte) ((v & 0xff00) >> 8),
                (byte) (v & 0xff) };
        return buf;
    }

    public static byte[] makeByte3(int v) {
        byte[] buf = { (byte) ((v & 0xff0000) >> 16),
                (byte) ((v & 0xff00) >> 8), (byte) (v & 0xff) };
        return buf;
    }

    public static byte[] makeByte2(int v) {
        byte[] buf = { (byte) ((v & 0xff00) >> 8), (byte) (v & 0xff) };
        return buf;
    }

    public static byte makeByte1(int v) {
        return (byte) (v & 0xff);
    }

    public static byte[] String2bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
