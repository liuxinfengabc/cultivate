package com.lz.ht.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Administrator
 */
@Data
public class AESEncripytData  implements Serializable {

   /**参数数据封装(使用AES 加密过的)*/
   private String  requestData ;

   /**使用RSA 公钥加密后的AES key*/
   private String aKey;

   /**使用RSA 公钥加密后的AES IV*/
   private String aIv;

   /**clientId,客户端ID*/
   private String clientId;



   public static void main(String[] args) throws Exception {
//      ParamsDemo p = new ParamsDemo();
//      p.setP1("casdfasdfasd");
//      p.setP2("早点睡了拉时代峻峰");
//      p.setP3(LocalDateTime.now().toString());
//      String toJSON = JSON.toJSONString(p);
//      String randomAesKey = KeysUtil.getRandomAesKey();
//      String randomAesIV = KeysUtil.getRandomAesIV();
//      /**参数数据封装(使用AES 加密过的)*/
//      String requestData = AESivUtil.encryptToBase64Str(toJSON, randomAesKey, randomAesIV);
//      /**使用RSA 公钥加密后的AES key*/
//      String encryptAESKey = RSAUtil.encrypt(randomAesKey, RSAUtil.getPublicKey(KeysUtil.RSA_PUBLIC_KEY));
//      /**使用RSA 公钥加密后的AES IV*/
//      String encryptAESIv = RSAUtil.encrypt(randomAesIV, RSAUtil.getPublicKey(KeysUtil.RSA_PUBLIC_KEY));
//      AESEncripytData  encripytData = new AESEncripytData();
//      encripytData.setAIv(encryptAESIv);
//      encripytData.setAKey(encryptAESKey);
//      encripytData.setRequestData(requestData);
//      String strToSend = JSON.toJSONString(encripytData);
//      System.out.println(strToSend);
//      //{"aIv":"zzEfnRJnPxQfNhMfWr23HEHJbmroQgjeghVMse6iTCUTZAAbbXtavC94H2rwnPWQa0rNSvW8iQKgDfL0WPGBoUCCGCqjwYMNsgBe9EsikC8GC5MIDiq5d6oDq6/R+i5c460Cpgz1GvctA/ckVD3zwpezSLRAp+TgxScdZfydaRI=","aKey":"aM+vkacBgs+dl+k3jhJgvuSQYA/i977TxOUzk9C21h6XTe/i7PymaleYLVAHkJUPo8dcgNPkyp2bdk4GBQdtb895ZhxiHPPfumIxVPKHta0G4FJuwOFmSjZ8j/4S12kvlYVVnscXZDMyrr1UB1zlR7GSrSLFkoqnIXoAPtdQ/Yw=","requestData":"P1+llzVB8PV4aPHTHQurX6cSAbFpVpTMvcCFG5Ime28qNaRE8PFXB3sgIobMiDOOTiQxHlKWRTMJUuNlnzcANh5kNWafrs+9C42bgVWWar8="}
   }


}
