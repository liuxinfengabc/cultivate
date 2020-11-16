package com.lz.ht.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author Administrator
 */
@Slf4j
public class JwtUtil {

    private static byte[]  mHMAC256Secret = "QJWEL1238129ZXR21LMBQEXPW932".getBytes();

    private static long defaultExpireTime = 10000;


    public static String createJWT(String username,String subject,long expireTime){
        if(expireTime<0){
            expireTime = defaultExpireTime;
        }
        JWTCreator.Builder builder = JWT.create();
        Date now = new Date();
        Date expireAt = getExpireAt(now,expireTime);
        String token  = builder.withClaim(username, false)
                .withIssuer("user")
                .withSubject(subject)
                .withExpiresAt(expireAt)
                .withIssuedAt(now)
                .sign(Algorithm.HMAC256(mHMAC256Secret));
        return token ;
    }

    public static  boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(mHMAC256Secret)).build();
            verifier.verify(token);
        } catch (Exception e) {
            log.info("",e);
            return false;
        }
        return true;
    }

    private static Date getExpireAt(Date now,long expireTime) {
        long time = now.getTime();
        long expire = time + expireTime;
        return new Date(expire);
    }

}
