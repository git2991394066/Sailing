package com.sailing.interfacetestplatform.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sailing.interfacetestplatform.dto.output.account.LoginOutputDto;

import java.util.Calendar;
import java.util.Map;

//session和【JWT】token二选一使用即可

/**
 * 【JWT】2、添加工具类
 * 生成Tooken的工具
 */
public class JWTUtil {
    // 用于JWT进行签名加密的秘钥
    private static String SECRET = "sailing-jwt-test-*%#@*!&";

    /**
     * @Param: 传入需要设置的payload信息
     * @return: 返回token
     */
    public static String generateToken(Map<String, String> tokenInfos) {
        JWTCreator.Builder builder = JWT.create();

        // 将map内的信息传入JWT的payload中
        tokenInfos.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        // 设置JWT令牌的过期时间为60分钟
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 7200);
        builder.withExpiresAt(instance.getTime());

        // 设置签名并返回token
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证token
     * @param token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 获取Payload信息
     * @param token
     * @return
     */
    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 从token获取当前登录信息
     * @param token
     * @return
     */
    public static LoginOutputDto getLoginData(String token)  {
        //获取当前用户的角色列表，使用通过验证的token中的载荷信息获取
        DecodedJWT decodedJWT = JWTUtil.getTokenInfo(token);
        String data = decodedJWT.getClaim("data").asString();

        LoginOutputDto loginOutputDto = null;
        try {
            loginOutputDto = new ObjectMapper().readValue(data, LoginOutputDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return loginOutputDto;
    }
}
