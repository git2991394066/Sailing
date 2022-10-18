package com.sailing.interfacetestplatform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.interfacetestplatform.service.InterfacesSynchronousService;
import com.sailing.interfacetestplatform.service.ModulesSynchronousService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/28 19:16
 * @description:
 **/
@Api(value="外部接口同步控制器",tags={"外部接口同步控制器"})
@RestController
@RequestMapping("/interfancesynchronous")
//@UserRight(roles = {"admin","staff"})
public class ModulesAndInterfaceSynchronousController {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ModulesSynchronousService modulesSynchronousService;
    @Autowired
    InterfacesSynchronousService interfacesSynchronousService;
//    @Autowired
//    HttpSession httpSession;

    /**
     * 输入可直接访问的swagger的api-docs 的url
     * 导入swagger中的模块和接口，不覆盖更新
     */

    @ApiOperation(value="不覆盖更新导入swagger中的模块和接口", notes="不覆盖更新导入swagger中的模块和接口")
    @GetMapping("/addSwaggerInterface")
    public String addSwaggerInterface(@RequestParam String swaggerUrl,Integer projectId){

///**
// * 第一步：请求输入的swagger api的完整url，获取返回体
// */
//        String url = swaggerUrl;
//
//////          带请求体的请求
////        System.out.println("url="+url);
////        String bodyStr ="";
////        RestTemplate restTemplate = new RestTemplate();
////        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
////        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
////        HttpEntity<String> httpEntity = new HttpEntity<>(bodyStr, httpHeaders);
////        ResponseEntity<String> responseEntity =
////                restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
////        String responseBody = String.valueOf(responseEntity.getBody());
////        System.out.println("带请求体的请求的响应"+responseBody);
////
////        //return responseBody;
//
//
////        不带请求体
////        //获取登录成功的票据cookie
////        String loginInfo = null;
////        if (httpSession.getAttribute("loginInfo") == null) {
////            loginInfo = getCookie();
////            httpSession.setAttribute("loginInfo", loginInfo);
////        } else {
////            loginInfo = httpSession.getAttribute("loginInfo").toString();
////        }
////        headers.set(HttpHeaders.COOKIE, loginInfo);
//
//        ////        System.out.println("getForEntity开始");  返回的响应，会把原响应的“”和：去掉，结果不是json
////        ResponseEntity<Object> responseEntity1
////                = restTemplate.getForEntity(swaggerUrl, Object.class);
////
////        //以下是getForEntity比getForObject多出来的内容
////        HttpStatus statusCode = responseEntity1.getStatusCode(); // 获取响应码
////        int statusCodeValue = responseEntity1.getStatusCodeValue(); // 获取响应码值
////        HttpHeaders headers = responseEntity1.getHeaders(); // 获取响应头
////        Object responseBodyObject = responseEntity1.getBody(); // 获取响应体对象
////        String responseBodyString = String.valueOf(responseEntity1.getBody());// 获取响应体字符串
////        Object responseObject = responseEntity1;//获取完整响应对象
////        String responseString=String.valueOf(responseEntity1);//获取完整响应字符串
////        System.out.println("HTTP 响应状态：" + statusCode);
////        System.out.println("HTTP 响应状态码：" + statusCodeValue);
////        System.out.println("HTTP Headers信息：" + headers);
////        System.out.println("HTTP Body Object："+responseBodyObject);
////        System.out.println("HTTP Body String："+responseBodyString);
////        System.out.println("HTTP Response Object："+responseObject);
////        System.out.println("HTTP Response String："+responseString);
////
//////        System.out.println("getForEntity结束");
////
////
//////        return responseBodyString;
////        return responseString;
//
////      System.out.println("getForObject开始");//返回的响应就是原响应
//        String responseStr = restTemplate.getForObject(swaggerUrl, String.class);
//        System.out.println("getForObject响应:"+responseStr);
////      System.out.println("getForObject结束");
//        return responseStr;
        /**
         * 第一步：请求输入的swagger api的完整url，获取返回体
         */
        String url = swaggerUrl;
//      System.out.println("getForObject开始");
        String responseStr = restTemplate.getForObject(swaggerUrl, String.class);
        //System.out.println("getForObject响应:"+responseStr);
//      System.out.println("getForObject结束");
        //将String转为json
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        //System.out.println("responseJson："+responseJson);

//        循环获取json的key和value
//        JSONObject res_data=  body.getJSONObject("data")；
//        Iterator sIterator = res_data.keySet().iterator();
//        while(sIterator.hasNext()){
//            Object key=sIterator.next();   //循环遍历每个key
//            res_data.getString("key");   //获取key里的value

//        // 定义一个字符串列表
//        List<String> modulesName = new ArrayList<String>();
//        //增加判断是否有tags
//        if(responseJson.getJSONArray("tags") != null) {
//            //模块
//            JSONArray tags = responseJson.getJSONArray("tags");//返回结果是列表
//            System.out.println("tags：" + tags);
//            //提取所有模块name到列表中
////            // 定义一个字符串列表
////            List<String> modulesName = new ArrayList<String>();
//            for (int i = 0; i < tags.size(); i++) {
//                modulesName.add(tags.getJSONObject(i).getString("name"));
//            }
//            System.out.println("共计有" + tags.size() + "个模块");
//            System.out.println("模块名列表详情为：" + modulesName);
//        }else{
//            modulesName=null;
//        }

        //模块
        //调整获取模块名的方案，由tags列表循环获取改为从paths.接口url.请求方法.tags[0]中获取
        // 定义一个字符串集合，提取模块名称并存储不重复的项
        Set<String> modulesNameSet = new HashSet<String>();
        JSONObject paths1=  responseJson.getJSONObject("paths");
        // 获取urls列表当作key值
        List<String> urls1=new ArrayList<String>();
        urls1=getJsonFirstFloorKey(paths1);
        for(int i=0;i<urls1.size();i++){
            //获取urls中的请求响应信息
            JSONObject requestAndResponse=  paths1.getJSONObject(urls1.get(i));
            //获取请求方法列表当作key值
            List<String> methods=new ArrayList<String>();
            methods=getJsonFirstFloorKey(requestAndResponse);
            for(int j=0;j<methods.size();j++){
                //获取请求方法中的请求响应信息
                JSONObject requestAndResponseInMethods=  requestAndResponse.getJSONObject(methods.get(j));
                //模块
                JSONArray tags = requestAndResponseInMethods.getJSONArray("tags");//返回结果是列表
                modulesNameSet.add((String) tags.get(0));
            }

        }
//        System.out.println("共计有" + modulesNameSet.size() + "个模块");
//        System.out.println("模块名列表详情集合为：" + modulesNameSet);
        // 定义一个字符串列表，存放模块名称，把集合转为列表，方便后续接口使用
        List<String> modulesName = new ArrayList<String>(modulesNameSet);
        System.out.println("模块名列表详情列表为：" + modulesName);
        System.out.println("共计有" + modulesName.size() + "个模块");

        //接口
        JSONObject paths=  responseJson.getJSONObject("paths");
        System.out.println("paths："+paths);
        //提取所有url到列表中
        // 定义一个字符串列表
        List<String> urls=new ArrayList<String>();
//        遍历获取json数据的所有key值

        urls=getJsonFirstFloorKey(paths);
//        Iterator sIterator2 = paths.keySet().iterator();
//        while(sIterator2.hasNext()){
//            Object key=sIterator2.next();   //循环遍历每个key
//            paths.getString("key");   //获取key里的value
//        }
        System.out.println("共计有"+urls.size()+"个url");
        System.out.println("接口url列表详情为："+urls);

        /**
         * 第二步 模块同步
         */
        //入参 模块名列表，项目Id
        if(modulesName!=null) {
            modulesSynchronousService.addSwaggerModules(modulesName, projectId);
        }

        /**
         * 第三步 接口同步
         */
        //入参 swagger响应体的paths、paths中提取的接口url列表、项目Id
        interfacesSynchronousService.addSwaggerInterfaces(paths,urls,projectId);

        return responseStr;



    }
    //        遍历获取json数据的第一层key值 方法，返回列表，兼容最外层是JSON和列表格式
    public static List<String> getJsonFirstFloorKey(Object obj){

        List<String> firstFloorKeyS=new ArrayList<String>();
        if(obj instanceof JSONObject){
//            System.out.println("外层是json的循环获取json第一层Key开始");
            for (Map.Entry<String, Object> entry : ((JSONObject) obj).entrySet()) {
//                System.out.println("循环获取json第一层Key中:"+entry.getKey());
                firstFloorKeyS.add(entry.getKey());
//                if(!(entry.getValue() instanceof String)){
//                    getJsonFirstFloorKey(entry.getValue(),entry.getKey());
//                }
            }
            //System.out.println("外层是json的所有key列表详情："+firstFloorKeyS);
//            System.out.println("外层是json的循环获取json第一层Key结束");
        }
        if(obj instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) obj;
//            System.out.println("外层是列表的循环获取json第一层Key开始");
            for (int i = 0;i < jsonArray.size();i++){
                getJsonFirstFloorKey(jsonArray.get(i));
            }
            System.out.println("外层是列表的所有key列表详情："+firstFloorKeyS);
//            System.out.println("外层是列表的循环获取json第一层Key结束");
        }
        return firstFloorKeyS;
    }

}
