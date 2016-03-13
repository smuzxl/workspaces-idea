package com.zxl.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    /**
     * @throws NoSuchAlgorithmException
     * @see #digest(String, CharSequence)
     */
    public static String md5(CharSequence cs) {
        try {
            return digest("MD5", cs);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * @throws NoSuchAlgorithmException
     * @see #digest(String, CharSequence)
     */
    public static String sha1(CharSequence cs) {
        try {
            return digest("SHA1", cs);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * 从字符串计算出数字签名
     *
     * @param algorithm 算法，比如 "SHA1" 或者 "MD5" 等
     * @param cs        字符串
     * @return 数字签名
     * @throws NoSuchAlgorithmException
     */
    public static String digest(String algorithm, CharSequence cs) throws NoSuchAlgorithmException {
        return digest(algorithm, getBytesUTF8(null == cs ? "" : cs), null, 1);
    }

    /**
     * 从字节数组计算出数字签名
     *
     * @param algorithm  算法，比如 "SHA1" 或者 "MD5" 等
     * @param bytes      字节数组
     * @param salt       随机字节数组
     * @param iterations 迭代次数
     * @return 数字签名
     * @throws NoSuchAlgorithmException
     */
    public static String digest(String algorithm, byte[] bytes, byte[] salt, int iterations) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        if (salt != null) {
            md.update(salt);
        }
        byte[] hashBytes = md.digest(bytes);
        for (int i = 1; i < iterations; i++) {
            md.reset();
            hashBytes = md.digest(hashBytes);
        }
        return fixedHexString(hashBytes);
    }

    private static String fixedHexString(byte[] hashBytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private static byte[] getBytesUTF8(CharSequence cs) {
        try {
            return cs.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
