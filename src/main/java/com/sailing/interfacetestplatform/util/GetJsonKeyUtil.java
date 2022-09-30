package com.sailing.interfacetestplatform.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/30 10:37
 * @description:
 **/
public class  GetJsonKeyUtil{
    //        遍历获取json数据的所有key值 方法，兼容最外层是JSON和列表格式
    public static void getJsonKey(Object obj,String listname){
        if(obj instanceof JSONObject){
            for (Map.Entry<String, Object> entry : ((JSONObject) obj).entrySet()) {
                System.out.println(listname+":"+entry.getKey());
                if(!(entry.getValue() instanceof String)){
                    getJsonKey(entry.getValue(),entry.getKey());
                }
            }
        }
        if(obj instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) obj;
            for (int i = 0;i < jsonArray.size();i++){
                getJsonKey(jsonArray.get(i),listname);
            }
        }
    }
}
