package com.cenrise.utils.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密器<br>
 * 
 * 默认128位加密
 * 
 * @author haojie.yuan
 * 
 */
public class AESUtil {

    /**
     * 加密
     * 
     * @param content
     *            待加密内容
     * @param key
     *            加密秘钥
     * @return 十六进制字符串
     */
    public static String encrypt(String content, String key) {

        try {
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, genKey(key));// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解密
     * 
     * @param content
     *            待解密内容(十六进制字符串)
     * @param key
     *            加密秘钥
     * @return
     */
    public static String decrypt(String content, String key) {

        try {
            byte[] decryptFrom = parseHexStr2Byte(content);
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, genKey(key));// 初始化
            byte[] result = cipher.doFinal(decryptFrom);
            return new String(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建加密解密密钥
     * 
     * @param key
     *            加密解密密钥
     * @return
     */
    private static SecretKeySpec genKey(String key) {
        byte[] enCodeFormat = { 0 };
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);// 默认128位，支持256位
            SecretKey secretKey = kgen.generateKey();
            enCodeFormat = secretKey.getEncoded();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SecretKeySpec(enCodeFormat, "AES");
    }

    /**
     * 将二进制转换成16进制
     * 
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    // 测试
    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();

        String key = "123123123123";
        String content = "asdasdasd";

        for (int i = 0; i < 10000; i++) {
            System.out.println("加密后前:" + content);
            String temp = encrypt(content, key);
            System.out.println("加密后:" + temp);
            String temp2 = decrypt(temp, key);
            System.out.println("解密后:" + temp2);
        }

        System.out.println("共用时:" + (System.currentTimeMillis() - startTime) + "ms");

    }

}
