package com.lz.ht.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Administrator
 */
public class  RSAUtil {


    /***
     * RSA 加密过程：
     * client  Server
     * client保存server提供的PUBLIC_KEY ,
     * client 使用公钥加密自己的请求体，发送到server端，
     * server 使用自己的私钥解密此包体
     */


//    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANbtBBL5nbbiIKJ8YsvpflP9opHRjcA1FrdTABGu+GQJHT5lOc70yVcFivhbHRrr4MRPX2h0PsZBONFrc4R/ph9X0Tikjhmk9x8Bo7yXIHHX+2qw5uj5sr1ZNFIGYI4ayHghiOmG5h+yM6OwkI3QnNngRrppZx5/LCu0+xKnYogXAgMBAAECgYBVdMQr70hAUUT8lkyKG3r5Vl3vcMzV4cX4em4KmfF1hRI1nvYPTdFIYpHREhlOfP9Pzud2ltHm7kPzMLArvL2F+lJnWkfeGfa5yNr4gxDHnx796SEuNIJdMUWkRgKHTcq9o0jiecKnR5XSW0MiwKz0L8zLfn0+5ZmdmvcAyQRtcQJBAOrXk/pR7k6OahyUn9F3pkzBT/7FCwJgdGbEXNbUvfG7dTImh8QNh+raur0Q7It4No9eGdGENyqko5xBRZSXug0CQQDqShZYJr+qYOOVapUPSgYT4Efs6Omj7X2+/Uswxq/L0YB4DdEzcL2PdMqujpoZqfbLhnNoqe3YXZwNwYZ+8fWzAkAwohxfBoCXfB61dIH0WxbxZkcut5ekEl2IurmPM9dyLCQFEr2i+DphhJKPfOW/rFGANziZjwvtpNLNFSdXsmQxAkEAjmkSQ+rpvgq+y2fJy01XfGVr0tmQixAkyx7zwWZEHUPvLwpXC0mQL87yFECh8s+ZMAU9yXThGg9seAAWXLt+eQJAZPjWR1y+vCYk9wU+ZsRmQ1cRf3otmnQiSeg3YIU8jBg8r1hpAXE1LGso79WAzJgt1X/1EsWYeLVVpUxqoz9XvA==";
//
//    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDW7QQS+Z224iCifGLL6X5T/aKR0Y3ANRa3UwARrvhkCR0+ZTnO9MlXBYr4Wx0a6+DET19odD7GQTjRa3OEf6YfV9E4pI4ZpPcfAaO8lyBx1/tqsObo+bK9WTRSBmCOGsh4IYjphuYfsjOjsJCN0JzZ4Ea6aWcefywrtPsSp2KIFwIDAQAB";


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     * 
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }


    /**
     * 获取私钥
     * 
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     * 
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }
    
    /**
     * RSA加密
     * 
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     * 
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容 
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     * 
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     * 
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }




    public static void main(String[] args) {
        try {
            KeyPair keyPair = getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//            // 生成密钥对
//            KeyPair keyPair = getKeyPair();
//            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
//            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
//            System.out.println("私钥:" + privateKey);
//            System.out.println("公钥:" + publicKey);
////             RSA加密
//            String data = "待加密的文字内容";
//            String encryptData = encrypt(data, getPublicKey(publicKey));
//            System.out.println("加密后内容:" + encryptData);
//            // RSA解密
//            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
//            System.out.println("解密后内容:" + decryptData);
//
//            // RSA签名
//            String sign = sign(data, getPrivateKey(privateKey));
//            // RSA验签
//            boolean result = verify(data, getPublicKey(publicKey), sign);
//            System.out.print("验签结果:" + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.print("加解密异常");
//        }
    }
}