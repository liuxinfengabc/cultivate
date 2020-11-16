package com.lz.ht.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * 解密对象
 */
@Data
public class AESDecripytData<T>   implements Serializable {

    /**解密之后的AES_KEY*/
    private String aesKey ;

    /**解密之后的AES_IV*/
    private String aesIv ;

    /**clientId,客户端ID*/
    private String clientId;



    /**解密之后的参数（JSON字符串）*/
    private String paramData;

    public AESDecripytData(){}

    public T getParamBean(Class<T> paramBeanClass){
        if(paramBeanClass!=null){
            try {
               // T o = (T)JSON.parseObject(paramData,paramBeanClass);
                //return o;
                return  null;
            } catch (Exception e) {
                throw new RuntimeException("class 类型不匹配");
            }
        }else {
            throw new RuntimeException("解密之后要封装成的对象的class为null");
        }
    }

}
