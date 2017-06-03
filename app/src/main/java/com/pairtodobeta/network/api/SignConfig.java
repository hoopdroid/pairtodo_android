package com.pairtodobeta.network.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SignConfig {

    public static final int APP_ID = 5;
    public static final String APP_LANG = "ru";
    public static final String OS = "android";


    public static String generateSig(ArrayList<String> args) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiManager.APP_TOKEN);
        stringBuilder.append(":");
        if (args.size()>0)
        for (int i = 0; i < args.size(); i++) {
            stringBuilder.append(args.get(i));
            stringBuilder.append(":");
        }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            String sig = stringBuilder.toString();

            return  generateSHA1(sig);

    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String generateSHA1(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

}
