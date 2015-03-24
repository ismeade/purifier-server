package com.ade.purifier.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

public class CryUtil {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CryUtil.class);

    public static String encrypt(String key, String context) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            Key ekey = readKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, ekey);
            byte[] b = context.getBytes("UTF-8");
            System.out.println(b.length);
            byte[] results = cipher.doFinal(b);
            System.out.println(results.length);
            return new String(results);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static String decrypt(String key, String context) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            Key ekey = readKey(key);
            cipher.init(Cipher.DECRYPT_MODE, ekey);
            byte[] results = cipher.doFinal(context.getBytes());
            return new String(results, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static void makeKey(String publicKeyPath, String privateKeyPath) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        saveKey(publicKey, publicKeyPath);
        saveKey(privateKey, privateKeyPath);
    }

    private static void saveKey(Key key, String keyName) throws Exception {
        FileOutputStream foskey = new FileOutputStream(keyName);
        ObjectOutputStream oos = new ObjectOutputStream(foskey);
        oos.writeObject(key);
        oos.close();
        foskey.close();
    }

    public static Key readKey(String keyName) throws Exception {
        if (keyName == null || keyName.trim().equals("") || !new File(keyName).exists() || new File(keyName).isDirectory()) {
            logger.error("key文件路径错误:" + keyName);
            return null;
        }
        FileInputStream fiskey = new FileInputStream(keyName);
        ObjectInputStream oiskey = new ObjectInputStream(fiskey);
        Key key = (Key) oiskey.readObject();
        oiskey.close();
        fiskey.close();
        return key;
    }

    public static void main(String[] args) {
//        try {
//            makeKey("c:/java/key.public", "c:/java/key.private");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String context = "{\"sn\":1416039235715,\"data\":{\"bindMacResp\":{\"message\":\"绑定成功\",\"code\":0}},\"version\":\"1.0-beta\"}";
        String context = "1234567";
        String tt = encrypt(CryUtil.class.getResource("/key.private").getPath(), context);
        System.out.println(tt);


        System.out.println(decrypt("c:/java/key.public", tt));
    }

}