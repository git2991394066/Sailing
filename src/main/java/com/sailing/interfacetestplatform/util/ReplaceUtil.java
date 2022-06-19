package com.sailing.interfacetestplatform.util;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 1:01:47
 * @description:内容替换工具类，主要包含手机号、名称
 **/
public class ReplaceUtil {
    public static String USER_NAME_KEY = "##username##";
    public static String PHONE_KEY = "##phone##";

    /**
     * 替换电话
     * @param source
     * @param repalceValue
     * @return
     */
    public static String replacePhone(String source,String repalceValue){
        return replace(source,PHONE_KEY,repalceValue);
    }

    /**
     * 替换用户名
     * @param source
     * @param repalceValue
     * @return
     */
    public static String replaceUserName(String source,String repalceValue){
        return replace(source,USER_NAME_KEY,repalceValue);
    }

    /**
     * 替换
     * @param source 源串
     * @param replaceKey 替换的key
     * @param repalceValue 替换的值
     * @return
     */
    public static String replace(String source,String replaceKey,String repalceValue){
        String target = null;
        if(source !=null){
            target = source.replaceAll(replaceKey,repalceValue);
        }
        return target;
    }
}

