package com.sailing.interfacetestplatform.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.util.ArrayList;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 1:02:13
 * @description:请求信息工具类，主要是通过JSONPath获取请求数据
 **/
public class RequestInfoUtil {
    private static String URL_KEY = "$..url";
    private static String METHOD_KEY = "$..method";
    private static String REQUEST_KEY = "$..request";
    private static String HEADERS_KEY = "$..headers";
    private static String DATA_KEY = "$..data";
    private static String PARAMS_KEY = "$..params";
    private static String JSON_KEY = "$..json";

    /**
     * 获取Url
     * @param requestData
     * @return
     */
    public static String getUrl(JSONObject requestData){
        String headers = getValue(requestData,URL_KEY);
        return headers;
    }

    /**
     * 获取请求方法
     * @param requestData
     * @return
     */
    public static String getMethod(JSONObject requestData){
        String headers = getValue(requestData,METHOD_KEY);
        return headers;
    }

    /**
     * 获取请求headers
     * @param requestData
     * @return
     */
    public static String getHeaders(JSONObject requestData){
        String request = getValue(requestData,REQUEST_KEY);
        JSONObject joRequest = JSONObject.parseObject(request);
        String headers = "";
        if(joRequest!=null) {
            headers = getValue(joRequest, HEADERS_KEY);
        }
        return headers;
    }

    /**
     * 获取请求数据
     * @param requestData
     * @return
     */
    public static String getData(JSONObject requestData){
        String request = getValue(requestData,REQUEST_KEY);
        JSONObject joRequest = JSONObject.parseObject(request);
        String datas = "";
        if(joRequest!=null) {
            datas = getValue(joRequest, DATA_KEY);
        }
        return datas;
    }

    /**
     * 获取请求参数
     * @param requestData
     * @return
     */
    public static String getParams(JSONObject requestData){
        String request = getValue(requestData,REQUEST_KEY);
        JSONObject joRequest = JSONObject.parseObject(request);
        String params = "";
        if(joRequest!=null) {
            params = getValue(joRequest, PARAMS_KEY);
        }
        return params;
    }

    /**
     * 获取请求JSON
     * @param requestData
     * @return
     */
    public static String getJson(JSONObject requestData){
        String request = getValue(requestData,REQUEST_KEY);
        JSONObject joRequest = JSONObject.parseObject(request);
        String jsons = "";
        if(joRequest!=null) {
            jsons = getValue(joRequest, JSON_KEY);
        }
        return jsons;
    }

    /**
     * 使用JSONPath获取指定key的数据
     * @param requestData
     * @param key
     * @return
     */
    public static String getValue(JSONObject requestData, String key){
        String result = null;
        ArrayList values = (ArrayList) JSONPath.read(requestData.toJSONString(),key);
        if(values!=null && values.size()>0){
            Object object = values.get(0);
            if(object instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) object;
                result = jsonObject.toJSONString();
            }else{
                result = object!=null?object.toString():"";
            }
        }



        return result;
    }
}

