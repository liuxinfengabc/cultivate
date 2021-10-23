package com.lz.ht.util;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


/**
 * @author Administrator
 */
public class AESivUtil {
    /**
     * AES 加密，带向量IV 版
     * AES_KEY 必须为128位（16字节），192位,256位
     * AES_IV 必须为 128位（16字节）
     * 注意使用UTF-8 编码的字符串来加解密
     */

    private static final String ALGORITHM_TYPE = "AES";

    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";

    private static final String AES_KEY = "ASDFQWERTYUIPLMN";

    private static final String AES_IV ="YUIPLMNASDFQWERT";

    private static  final Charset CHARSET = Charset.forName("UTF-8");

    private static byte[] key(String AESkey) {
        if(AESkey!=null ){
            byte[] bytes = AESkey.getBytes(CHARSET);
            int len =bytes.length;
            if(len ==16 || len==24 || len==32){
                return bytes;
            }
        }
        return null;
    }

    private static byte[] iv(String aesIv) {
        if(aesIv!=null ){
            byte[] bytes = aesIv.getBytes(CHARSET);
            int len = bytes.length;
            if(len == 16 ){
                return bytes;
            }
        }
        return null;
    }

    private static Cipher getCipher(String sKey,String siv,Integer encryptOrDecrpty)  {
        byte[] raw = key(sKey);
        byte[] ivByte = iv(siv);
        try {
            if(raw!=null && ivByte!=null){
                SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM_TYPE);
                Cipher cipher = Cipher.getInstance(CIPHER_MODE);
                IvParameterSpec iv = new IvParameterSpec(ivByte);
                cipher.init(encryptOrDecrpty, skeySpec, iv);
                return cipher;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 加密算法
     * @param sSrc  明文
     * @param sKey  密钥
     * @param siv   初始向量IV
     * @return
     * @throws Exception
     */
    public static  byte[]  encrypt(byte[] sSrc, String sKey,String siv ) throws Exception {
        Cipher cipher = getCipher(sKey, siv,Cipher.ENCRYPT_MODE);
        if(cipher==null){
            return null;
        }
        byte[] encrypted = cipher.doFinal(sSrc);
        return encrypted;
    }


    /**
     * 解密算法
     * @param sSrc  密文
     * @param sKey  密钥
     * @param siv   初始向量IV
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] sSrc, String sKey ,String siv) throws Exception {
            Cipher cipher = getCipher(sKey, siv,Cipher.DECRYPT_MODE);
            if(cipher==null){
                return null;
            }
            byte[] original = cipher.doFinal(sSrc);
            return original;
    }



    public static String  encryptToBase64Str(String sSrc, String sKey,String siv ) throws Exception{
        if( isEmpty(sSrc)){
            return null;
        }
        byte[] encrypt = encrypt(sSrc.getBytes(CHARSET), sKey, siv);
        return Base64.getEncoder().encodeToString(encrypt);
    }


    public static String decryptToStr(String encryptStr, String sKey ,String siv)throws Exception{
        if( isEmpty(encryptStr)){
            return null;
        }
        byte[] decode = Base64.getDecoder().decode(encryptStr);
        byte[] decrypt = decrypt(decode, sKey, siv);
        return new String(decrypt,CHARSET);
    }


    /**
     * 判断是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        // 如果字符串不为null，去除空格后值不与空字符串相等的话，证明字符串有实质性的内容
        if (str != null && !str.isEmpty()) {
            // 不为空
            return false;
        }
        // 为空
        return true;
    }


    /**
     * 获取随机位数的字符串
     * @author Rlon
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {
        String  str = "ASDFQW撒发到付！@@#￥%……&&**（（）（）（）&&*&阿萨，asfpqw}{}09(U87878h46%……￥￥77&ERTYUIPLMN撒发到付阿萨asdfasdf撒发到付阿萨asdfewewklasdfjklsdafo的撒发到付阿萨德快来发生的覅we";
        String toBase64Str = encryptToBase64Str(str, AES_KEY, AES_IV);
        System.out.println(toBase64Str);
        String decryptToStr = decryptToStr(toBase64Str, AES_KEY, AES_IV);
        System.out.println(decryptToStr);

    }

}
