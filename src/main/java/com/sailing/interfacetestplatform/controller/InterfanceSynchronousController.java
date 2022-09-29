package com.sailing.interfacetestplatform.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/28 19:16
 * @description:
 **/
@Api(value="外部接口同步控制器",tags={"外部接口同步控制器"})
@RestController
@RequestMapping("/interfancesynchronous")
//@UserRight(roles = {"admin","staff"})
public class InterfanceSynchronousController {
    @Autowired
    RestTemplate restTemplate;
//    @Autowired
//    HttpSession httpSession;

    /**
     * 输入可直接访问的swagger的api-docs 的url
     * 导入swagger中的模块和接口，不覆盖更新
     */

    @ApiOperation(value="不覆盖更新导入swagger中的模块和接口", notes="不覆盖更新导入swagger中的模块和接口")
    @GetMapping("/addSwaggerInterface")
    public String addSwaggerInterface(@RequestParam String swaggerUrl){

/**
 * 第一步：请求输入的swagger api的完整url，获取返回体
 */
        String url = swaggerUrl;
//          带请求体的请求
//        System.out.println("url="+url);
//        String bodyStr ="";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
//        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//        HttpEntity<String> httpEntity = new HttpEntity<>(bodyStr, httpHeaders);
//        ResponseEntity<String> responseEntity =
//                restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//        String responseBody = String.valueOf(responseEntity.getBody());
//        System.out.println(responseBody);

        //return responseBody;

//        不带请求体
//        //获取登录成功的票据cookie
//        String loginInfo = null;
//        if (httpSession.getAttribute("loginInfo") == null) {
//            loginInfo = getCookie();
//            httpSession.setAttribute("loginInfo", loginInfo);
//        } else {
//            loginInfo = httpSession.getAttribute("loginInfo").toString();
//        }
//        headers.set(HttpHeaders.COOKIE, loginInfo);

//        System.out.println("getForEntity开始");
        ResponseEntity<Object> responseEntity1
                = restTemplate.getForEntity(swaggerUrl, Object.class);

        //以下是getForEntity比getForObject多出来的内容
        HttpStatus statusCode = responseEntity1.getStatusCode(); // 获取响应码
        int statusCodeValue = responseEntity1.getStatusCodeValue(); // 获取响应码值
        HttpHeaders headers = responseEntity1.getHeaders(); // 获取响应头
        Object responseBodyObject = responseEntity1.getBody(); // 获取响应体
        String responseBodyString = String.valueOf(responseEntity1.getBody());
        System.out.println("HTTP 响应状态：" + statusCode);
//        System.out.println("HTTP 响应状态码：" + statusCodeValue);
//        System.out.println("HTTP Headers信息：" + headers);
        System.out.println("HTTP Body Object："+responseBodyObject);
        System.out.println("HTTP Body String："+responseBodyString);

//        System.out.println("getForEntity结束");


        return responseBodyString;
    }

}
