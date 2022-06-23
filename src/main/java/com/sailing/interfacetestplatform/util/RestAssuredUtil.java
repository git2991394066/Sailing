package com.sailing.interfacetestplatform.util;

import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 1:02:53
 * @description:RestAssured请求工具类，主要包装了RestAssured的请求
 **/
public class RestAssuredUtil {
    /**
     * 执行一次请求
     * @param requestData
     * @return
     */
    public static Response request(JSONObject requestData){
        String url = RequestInfoUtil.getUrl(requestData);
        String method = RequestInfoUtil.getMethod(requestData);
        String headers = RequestInfoUtil.getHeaders(requestData);
        String params = RequestInfoUtil.getParams(requestData);
        String json = RequestInfoUtil.getJson(requestData);

        //params转成RestAssured需要的Map
        Map<String,Object> paramsMap = JSONObject.parseObject(params.isEmpty()?"{}":params,Map.class);
        //Headers转成RestAssured需要的Map
        Map<String,Object> headersMap = JSONObject.parseObject(headers.isEmpty()?"{}":headers,Map.class);
        Response response = null;
        switch (method.toLowerCase()){
            case "get":
                response = given().log().all().headers(headersMap).params(paramsMap).when().get(url).then().log().all().extract().response();
                break;
            case "post":
                response= given().log().all().headers(headersMap).pathParams(paramsMap).params(paramsMap).body(json).when().post(url).then().log().all().extract().response();
                break;
            case "put":
                response = given().log().all().headers(headersMap).pathParams(paramsMap).params(paramsMap).body(json).when().put(url).then().log().all().extract().response();
                break;
            case "patch":
                response = given().log().all().headers(headersMap).pathParams(paramsMap).params(paramsMap).body(json).when().patch(url).then().log().all().extract().response();
                break;
            case "delete":
                response = given().log().all().headers(headersMap).pathParams(paramsMap).params(paramsMap).body(json).when().delete(url).then().log().all().extract().response();
                break;
            case "head":
                response = given().log().all().headers(headersMap).pathParams(paramsMap).params(paramsMap).body(json).when().head(url).then().log().all().extract().response();
                break;
            case "options":
                response = given().log().all().headers(headersMap).pathParams(paramsMap).params(paramsMap).body(json).when().options(url).then().log().all().extract().response();
                break;
            default:
                response = given().log().all().headers(headersMap).pathParams(paramsMap).when().get(url).then().log().all().extract().response();
                break;
        }
        return response;
    }
}

