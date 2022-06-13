package com.sailing.interfacetestplatform.util;

import java.security.MessageDigest;

//session和【JWT】token二选一使用即可

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 17:43:59
 * @description:密码加密工具
 **/

public class PasswordUtil {

    public static final String encrypt(String password, String salt){
        try{
            //MessageDigest是封装md5算法的工具对象还支持SHA算法
            MessageDigest md=MessageDigest.getInstance("SHA");
            //通过digest拿到的任意字符串,得到的bates都是等长的
            byte[] bytes=md.digest((password+salt).getBytes("utf-8"));
            return toHex(bytes);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }
    /**
     * 二进制的加密字符串转成16进制
     * @param bytes
     * @return
     */
    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS="0123456789ABCDEF".toCharArray();
        StringBuilder result=new StringBuilder(bytes.length*2);
        for(int i=0;i<bytes.length;i++){
            result.append(HEX_DIGITS[(bytes[i]>>4) & 0x0f]);
            result.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return result.toString();
    }
}
