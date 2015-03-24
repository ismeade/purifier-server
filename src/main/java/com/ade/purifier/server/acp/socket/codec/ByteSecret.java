package com.ade.purifier.server.acp.socket.codec;

import com.ade.purifier.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ismeade on 2014/11/3.
 */
public class ByteSecret {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(ByteSecret.class);

    private static byte[] key;

    private final static String FILE_NAME  = "/purifier.key";
    private final static int    KEY_LENGTH = 256;
    private final static int    USE_LENGTH = 10;

    public static boolean initialize() {
        logger.info("load file: " + FILE_NAME);
        InputStream in = null;
        try {
            in = ByteSecret.class.getResourceAsStream(FILE_NAME);
            StringBuffer keys = new StringBuffer();
            byte[] tempbytes = new byte[KEY_LENGTH * 2];
            in.read(tempbytes);
            for (int i = 0; i < tempbytes.length; i++) {
                keys.append((char)tempbytes[i]);
            }
            key = ByteUtils.String2bytes(keys.toString());
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return isSuccess();
    }

    private static boolean isSuccess() {
        return key != null && key.length == KEY_LENGTH;
    }

    public static void encrypt(byte[] bytes) {
        if (isSuccess()) {
            byte chack = bytes[bytes.length - 2];
            byte[] use = getUseKey(chack);
            editor(bytes, use);
        }
    }

    public static void decrypt(byte[] bytes) {
        if (isSuccess()) {
            byte chack = bytes[bytes.length - 2];
            byte[] use = getUseKey(chack);
            editor(bytes, use);

        }
    }

    private static byte[] getUseKey(byte in) {
        int index = ByteUtils.makeUint16((byte) 0x00, in);
        byte[] use = new byte[USE_LENGTH];
        for (int i = 0; i < use.length; i++) {
            if (index == KEY_LENGTH) {
                index = 0;
            }
            use[i] = key[index];
            index++;
        }
        return use;
    }

    public static byte getCheck(byte[] bytes) {
        byte temp = (byte) 0x00;
        for (int i = 2; i < bytes.length - 2; i++) {
            temp = (byte) (temp ^ bytes[i]);
        }
        return temp;
    }

    public static byte getRandom () {
        return (byte) (Math.random() % 255);
    }

    public static void editor(byte[] bytes, byte[] use) {
        for (int i = 2; i < bytes.length - 2; i++) {
            for (int j = 0; j < use.length; j++) {
                bytes[i] = (byte) (bytes[i] ^ use[j]);
            }
        }
    }

    public static void main(String[] args) {
        initialize();
        byte[] b = {(byte)0x02, (byte)0x02, (byte)0x31, (byte)0x01, (byte)0x00, (byte)0x30, (byte)0x03};
        byte[] use = getUseKey((byte)0x30);
        System.out.println(ByteUtils.toStr(use));
        editor(b, use);
        System.out.println(ByteUtils.toStr(b));

        byte tb = (byte) 0x00;
        for (int i = 0; i < use.length; i++) {
            tb = (byte)(tb ^ use[i]);
            System.out.println(tb);
        }
//        System.out.println(getCheck(b));
//        System.out.println(MessageUtils.makeUint16((byte)0x00, (byte)((byte)0xB4 ^ (byte)0x01)));
    }

}
