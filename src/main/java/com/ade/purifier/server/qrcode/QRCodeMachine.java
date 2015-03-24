package com.ade.purifier.server.qrcode;

import com.ade.purifier.orm.ObjectContextFactory;
import com.ade.purifier.orm.dao.MacInfoDao;
import com.ade.purifier.utils.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.*;
import java.util.Date;
import java.util.Hashtable;

/**
 * Created by ismeade on 2014/12/23.
 */
public class QRCodeMachine {

    private MacInfoDao macInfoDao;

    private String path;

    private final static String FORMAT_DEFAULT = "jpg";

    private final static int SIZE_DEFAULT = 400;
    private final static String CHARACTER = "utf-8";

    public QRCodeMachine(String path) {
        this.path = path;
        this.path = this.path.endsWith(File.separator) ? this.path : this.path + File.separator;
        System.out.println("path:" + this.path);
        File file = new File(this.path);
        file.mkdirs();
        System.out.println();
        macInfoDao = new MacInfoDao(ObjectContextFactory.createObjectContext());
    }

    public void createQRCode(String mac) {
        if (mac == null || "".equals(mac.trim())) {
            return;
        }
        String md5 = StringUtils.MD5(mac + new Date().toString() + "vs.touchcell.cn");
        int id = macInfoDao.registereMacInfo(mac, md5);
        if (id <= 0) {
            System.out.println("mac:" + mac + "is exist.");
        } else {
            System.out.println("mac:" + mac + " md5:" + md5);
            create(md5, path + mac + "_" + md5 + "." + FORMAT_DEFAULT, SIZE_DEFAULT);
        }

    }

    private void create(String context, String path, int size) {
        if (context == null || "".equals(context.trim())) {
            return;
        }
        try {
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, CHARACTER);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(context, BarcodeFormat.QR_CODE, size, size, hints);
            File outputFile = new File(path);
            MatrixToImageWriter.writeToFile(bitMatrix, FORMAT_DEFAULT, outputFile);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    public static void main(String[] args) {
        QRCodeMachine q = new QRCodeMachine("c:/java/temp/QRCode1");
        try {
            FileReader reader = new FileReader("C:/Users/ismeade/Desktop/mac.txt");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while((str = br.readLine()) != null) {
                System.out.println("read <- " + str);
                if (str.trim().length() == 12) {
                    q.createQRCode(str);
                }
            }
            br.close();
            reader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }


//        String mac = "08002859322C";
//        String md5 = "c60bcfc96a7d8c2fdac642f40d432abd";
//
//        try {
//            Hashtable hints = new Hashtable();
//            hints.put(EncodeHintType.CHARACTER_SET, CHARACTER);
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(md5, BarcodeFormat.QR_CODE, 400, 400, hints);
//            File outputFile = new File("c:/java/temp/QRCode1/" + mac + "_" + md5 + ".jpg");
//            MatrixToImageWriter.writeToFile(bitMatrix, FORMAT_DEFAULT, outputFile);
//        } catch (Exception e) {
//            System.out.println(e.getLocalizedMessage());
//        }
    }

}
