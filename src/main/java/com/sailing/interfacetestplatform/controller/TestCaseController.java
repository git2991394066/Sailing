package com.sailing.interfacetestplatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.testcase.TestCaseCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.testcase.TestCaseUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.testcase.TestCaseOutputDto;
import com.sailing.interfacetestplatform.service.TestCaseService;
import com.sailing.interfacetestplatform.util.RandomUtil;
import com.sailing.interfacetestplatform.util.ReplaceUtil;
import com.sailing.interfacetestplatform.util.RestAssuredUtil;
import io.restassured.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.lucene.util.RamUsageEstimator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/20/0020 0:48:03
 * @description:测试用例控制器
 **/
@Api(value="测试用例控制器",tags={"测试用例控制器"})
@RestController
@RequestMapping("/testcase")
@UserRight(roles = {"admin","staff"})
public class TestCaseController {
    @Autowired
    TestCaseService testCasesService;

    @ApiOperation(value="按条件分页查询", notes="按条件分页查询")
    @GetMapping("/")
    public ResponseData<List<TestCaseOutputDto>> query(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam Integer interfaceId, @RequestParam Integer testSuitId, Integer taskId, Integer projectId){
        return testCasesService.query(pageIndex,pageSize,interfaceId,testSuitId,taskId,projectId);
    }

    @ApiOperation(value="查询全部", notes="查询全部")
    @GetMapping("/queryByProjectId")
    public ResponseData<List<TestCaseOutputDto>> queryByProjectId(@RequestParam Integer projectId){
        return testCasesService.queryByProjectId(projectId);
    }

    @ApiOperation(value="根据ID获取", notes="根据ID获取")
    @GetMapping("/{id}")
    public ResponseData<TestCaseOutputDto> getById(@PathVariable Integer id){
        return testCasesService.getById(id);
    }

    @ApiOperation(value="创建接口", notes="创建接口")
    @PostMapping("/")
    public ResponseData<TestCaseOutputDto> create(@RequestBody @Validated TestCaseCreateInputDto inputDto){
        return testCasesService.create(inputDto);
    }

    @ApiOperation(value="修改接口", notes="修改接口")
    @PutMapping("/")
    public ResponseData<TestCaseOutputDto> update(@RequestBody @Validated TestCaseUpdateInputDto inputDto){
        return testCasesService.update(inputDto);
    }

    @ApiOperation(value="根据ID删除接口", notes="根据ID删除接口")
    @DeleteMapping("/{id}")
    public ResponseData<Boolean> delete(@PathVariable Integer id){
        return testCasesService.delete(id);
    }

    @ApiOperation(value="拷贝接口", notes="拷贝接口")
    @PutMapping("/copy")
    public ResponseData<TestCaseOutputDto> copy(@RequestBody @Validated TestCaseCreateInputDto inputDto){
        return testCasesService.copy(inputDto);
    }

    @ApiOperation(value="发送一个请求", notes="发送一个请求")
    @PostMapping("/run")
    public ResponseData<Object> run(@RequestBody JSONObject requestData){
        //内容替换
        String phone = RandomUtil.randomPhone();
        requestData = JSONObject.parseObject(ReplaceUtil.replacePhone(requestData.toString(),phone));
        String userName = RandomUtil.randomUsername();
        requestData = JSONObject.parseObject(ReplaceUtil.replaceUserName(requestData.toString(),userName));

        Response response = RestAssuredUtil.request(requestData);
//        //打印响应时间和响应大小 v1.0.1
//        Long responseTimeMs = response.time();
//        System.out.println("Response time in ms using time():"+responseTimeMs);
//        Long responseTimeS = response.timeIn(TimeUnit.SECONDS);
//        System.out.println("Response time in seconds using timeIn():"+responseTimeS);

        //v1.0.1计算响应对象大小
//        响应大小有的响应有content-length可以直接取，有些没有这个字段需要自己处理

//        if(response.getHeader("Content-Length")!=null){
//        Long contentLength=Long.parseLong(response.getHeader("Content-Length"));
//        System.out.println("Response Content-Length:"+contentLength);}

//        //调试计算对象大小
//        class ObjectA {
//            byte b1; // 1
//            String str;  // 4
//            int i1; // 4
//            byte b2; // 1
//            int i2;  // 4
//            byte b3;  // 1
//            long l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14,l15,l16,l17,l18,l19,l20;//8
//        }
//        System.out.println("调试对象大小为:"+RamUsageEstimator.sizeOf(new ObjectA())+"B 字节");
//
//        Integer integer = new Integer(10);
//        System.out.println("指定对象本身在堆空间的大小"+RamUsageEstimator.shallowSizeOf(integer));

        Long responseBytes=RamUsageEstimator.sizeOf(response.asByteArray());
        System.out.println("响应大小为:"+ responseBytes+"Bytes 字节");

        //计算指定对象本身在堆空间的大小，单位字节。不是整体大小 //输出24
        System.out.println("指定对象本身在堆空间的大小" + RamUsageEstimator.shallowSizeOf(response));
        //计算指定对象及其引用树上的所有对象的综合大小，单位字节。是整体大小  输出实际响应字节数
        System.out.println("指定对象及其引用树上的所有对象的综合大小" + RamUsageEstimator.sizeOf(response.asByteArray()));



        JSONObject result = new JSONObject();
        result.put("status_code", "200");
        result.put("headers",response.getHeaders());
        result.put("cookies",response.getCookies());

        //把响应时间和大小加入运行结果 v1.0.1
        result.put("responseTimeMs",response.time());
        result.put("responseTimeS",response.timeIn(TimeUnit.SECONDS));
//        if(response.getHeader("Content-Length")!=null){
//        result.put("contentLength",Long.parseLong(response.getHeader("Content-Length")));}
        if(responseBytes!=null){
        result.put("contentLength",responseBytes);}

        try {
            result.put("json", response.jsonPath().get());
        }catch (Exception ex){
            result.put("json","");
            result.put("body",response.getBody().print());
        }
        return   ResponseData.success(result);
    }
}
