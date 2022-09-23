package com.sailing.interfacetestplatform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sailing.interfacetestplatform.dto.output.environment.EnvironmentOutputDto;
import com.sailing.interfacetestplatform.dto.output.interf.InterfaceOutputDto;
import com.sailing.interfacetestplatform.dto.output.testcase.TestCaseOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestResultCaseCommonDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestResultCaseOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestResultOutputDto;
import com.sailing.interfacetestplatform.dto.output.testreport.TestResultSuitOutputDto;
import com.sailing.interfacetestplatform.entity.*;
import com.sailing.interfacetestplatform.service.TaskService;
import com.sailing.interfacetestplatform.service.TestRecordService;
import com.sailing.interfacetestplatform.service.TestReportService;
import com.sailing.interfacetestplatform.service.TestSuitService;
import com.sailing.interfacetestplatform.util.RandomUtil;
import com.sailing.interfacetestplatform.util.ReplaceUtil;
import com.sailing.interfacetestplatform.util.RestAssuredUtil;
import com.sailing.interfacetestplatform.util.SessionUtil;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/24/0024 22:53:21
 * @description:测试任务测试服务，主要处理一次测试任务下所有关联测试用例，在指定项目环境真实的测试
 **/
@Service
public class TaskTestService {
    @Autowired
    CacheManager cacheManager;
    @Autowired
    TestRecordService testRecordService;
    @Autowired
    TestReportService testReportService;
    @Autowired
    TestSuitService testSuitService;
    @Autowired
    TaskService taskService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;

    //字节数组输出流，用于RestAssured关联的PrintStream流输出接口调用返回数据存放
    private ByteArrayOutputStream byteArrayOutputStream ;

    //响应提取全局缓存键，第一个参数为recordId，表示当前测试用例所在的测试记录ID
    private static String GLOBAL_CACHE_NAME = "task_%d";

    /**
     * 测试
     * @param environmentEntity
     * @param interfaceEntities
     * @param testCaseEntities
     */
    @Transactional
    public void test(EnvironmentEntity environmentEntity, List<InterfaceEntity> interfaceEntities, List<TestCaseEntity> testCaseEntities, TestRecordEntity testRecordEntity, SessionUtil.CurrentUser currentUser) throws JsonProcessingException {
        //1、设置RestAssured日志输出流，用于输出接口调用日志
        byteArrayOutputStream =  new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(printStream));


        //2、对测试用例按照优先级列排序
        testCaseEntities = testCaseEntities.stream().sorted(Comparator.comparing(TestCaseEntity::getOrderIndex)).collect(Collectors.toList());
        //按优先顺序执行用例
        List<TestResultCaseOutputDto> testResultCaseOutputDtos = new ArrayList<>();
        for(TestCaseEntity testCaseEntity: testCaseEntities){
            InterfaceEntity currentInterfaceEntity = interfaceEntities.stream().filter(s->s.getId() == testCaseEntity.getInterfaceId()).findFirst().orElse(null);
            if(currentInterfaceEntity!=null){
                //对测试用例进行测试
                //v1.0.1 用例响应时间在testResultCaseOutputDto的responseData下
                TestResultCaseOutputDto testResultCaseOutputDto = this.testCase(environmentEntity,currentInterfaceEntity,testCaseEntity,testRecordEntity);

                testResultCaseOutputDtos.add(testResultCaseOutputDto);
            }
        }
        //3、关闭输出流
        try {
            printStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4、组织测试结果
        List<Integer> testSuitIds = testCaseEntities.stream().map(s->s.getTestSuitId()).distinct().collect(Collectors.toList());
        TestResultOutputDto testResultOutputDto = new TestResultOutputDto();
        testResultOutputDto.setTaskId(testRecordEntity.getTaskId());
        TaskEntity taskEntity = taskService.getById((Serializable)testRecordEntity.getTaskId());
        if(taskEntity!=null){
            //测试任务名
            testResultOutputDto.setTaskName(taskEntity.getName());
        }
        //测试套件数
        testResultOutputDto.setTotalOfTestSuit((long)testSuitIds.size());
        //测试用例数
        testResultOutputDto.setTotalOfTestCase((long)testCaseEntities.size());
        //成功用例数
        testResultOutputDto.setTotalOfTestCaseForSuccess(testResultCaseOutputDtos.stream().filter(s->s.getStatus().intValue() == 0).count());
        //失败用例数
        testResultOutputDto.setTotalOfTestCaseForFailure(testResultCaseOutputDtos.stream().filter(s->s.getStatus().intValue() == 1).count());
        //异常用例数
        testResultOutputDto.setTotalOfTestCaseForError(testResultCaseOutputDtos.stream().filter(s->s.getStatus().intValue() == 2).count());
        //测试任务运行用时
        double totalDuration = testResultCaseOutputDtos.stream().mapToDouble(s -> ((double)(s.getEndTime().getTime()-s.getStartTime().getTime())/1000)).sum();
        testResultOutputDto.setTotalDuration(totalDuration);
        //根据所有用例结果判断测试是否成功
        int status =0;
        if(testResultOutputDto.getTotalOfTestCase().intValue()>0) {
            //如果成功的测试用例数量为0，状态为2，失败
            if (testResultOutputDto.getTotalOfTestCaseForSuccess().intValue() <= 0) {
                status = 2;
            } else {
                //如果成功有测试用例数不为0，但又比测试用例少，状态为1，表示部分成功
                if (testResultOutputDto.getTotalOfTestCaseForSuccess().intValue() < testResultOutputDto.getTotalOfTestCase().intValue()) {
                    status = 1;
                }
            }
        }
        testResultOutputDto.setStatus(status);
        //设置当前测试的项目环境快照，因为测试完后，相应数据可能会被修改
        testResultOutputDto.setEnvironment(modelMapper.map(environmentEntity, EnvironmentOutputDto.class));

        //5、组织测试套件结果集合，根据用例测试返回的测试用例结果，组织成测试任务->测试套件结果->测试用例结果结构
        QueryWrapper<TestSuitEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", testSuitIds);
        queryWrapper.eq("is_delete", false);
        List<TestSuitEntity> testSuitEntities = testSuitService.list(queryWrapper);
        if(testSuitEntities!=null && testSuitEntities.size()>0){
            List<TestResultSuitOutputDto> testResultSuitOutputDtos = new ArrayList <>();
            for(TestSuitEntity testSuitEntity: testSuitEntities) {
                //组织单个测试套件
                TestResultSuitOutputDto testResultSuitOutputDto = new TestResultSuitOutputDto();
                testResultSuitOutputDto.setTestSuitId( testSuitEntity.getId());
                testResultSuitOutputDto.setTestSuitName(testSuitEntity.getName());

                //获取关联测试用例测试结果
                //todo  每个测试用例增加响应时间查看  用例响应时间在testResultCaseOutputDto的responseData下
                List<TestResultCaseOutputDto> currentTestResultCaseOutputDtos = testResultCaseOutputDtos.stream().filter(s->s.getTestCase().getTestSuitId().intValue() == testSuitEntity.getId()).collect(Collectors.toList());
                testResultSuitOutputDto.setTestCaseResults(currentTestResultCaseOutputDtos);

                testResultSuitOutputDtos.add(testResultSuitOutputDto);
            }
            testResultOutputDto.setTestSuitResults(testResultSuitOutputDtos);
        }

        //6、添加一条测试报告,其中测试结果保存了组织的测试结果+快照数据
        TestReportEntity testReportEntity = new TestReportEntity();
        testReportEntity.setResult(objectMapper.writeValueAsString(testResultOutputDto));
        testReportEntity.setIsDelete(false);
        testReportEntity.setTestRecordId(testRecordEntity.getId());
        testReportEntity.setProjectId(testRecordEntity.getProjectId());
        //由于拦截添加创建和修改信息无法在多线程共享会话，需要手动维护创建和修改信息
        Date current = new Date();
        UserEntity userEntity = currentUser.getUserEntity();
        testReportEntity.setCreateById(userEntity.getId());
        testReportEntity.setCreateByName(userEntity.getName());
        testReportEntity.setCreateTime(current);
        testReportEntity.setUpdateById(userEntity.getId());
        testReportEntity.setUpdateByName(userEntity.getName());
        testReportEntity.setUpdateTime(current);

        //v1.0.1待定，不好循环提取
        // testReportEntity.setUserDefinedResponse();
        testReportService.save(testReportEntity);

        //7、修改测试记录的状态为完成
        testRecordEntity.setStatus(0);
        //由于拦截添加创建和修改信息无法在多线程共享会话，需要手动维护创建和修改信息
        testRecordEntity.setUpdateById(userEntity.getId());
        testRecordEntity.setUpdateByName(userEntity.getName());
        testRecordEntity.setUpdateTime(current);
        testRecordService.updateById(testRecordEntity);
    }

    /**
     * 测试单个测试用例
     * @param environmentEntity
     * @param interfaceEntity
     * @param testCaseEntity
     * @param testRecordEntity
     * @return
     */
    private TestResultCaseOutputDto testCase(EnvironmentEntity environmentEntity, InterfaceEntity interfaceEntity, TestCaseEntity testCaseEntity,
                                             TestRecordEntity testRecordEntity){
        TestResultCaseOutputDto testResultCaseOutputDto = new TestResultCaseOutputDto();

        //0.设置测试结果中的用例信息基础数据快照，因为测试完成后，可能相应数据会变化
        testResultCaseOutputDto.setTestCase(modelMapper.map(testCaseEntity, TestCaseOutputDto.class));
        testResultCaseOutputDto.setInf(modelMapper.map(interfaceEntity, InterfaceOutputDto.class));
        //1、随机内容替换
        this.randomReplace(interfaceEntity,testCaseEntity,testRecordEntity);
        //2、全局响应提取内容替换
        this.globalExtractReplace(interfaceEntity,testCaseEntity,testRecordEntity);

        //3、根据测试用例、测试环境构造请求数据
        JSONObject requestData = new JSONObject();
        requestData.put("url",environmentEntity.getHost()+interfaceEntity.getPath());
        requestData.put("method",interfaceEntity.getRequestMethod());
        requestData.put("request",testCaseEntity.getRequestData());

        JSONObject responseData = null;

        //调用开始时间
        Date startTime = new Date();
        try {
            //4、使用RestAssured进行接口调用
            Response response = RestAssuredUtil.request(requestData);
            //调用结束时间
            Date endTime = new Date();
            responseData = new JSONObject();
            responseData.put("status_code", response.getStatusCode());
            responseData.put("headers",response.getHeaders());
            responseData.put("cookies",response.getCookies());
            responseData.put("json",requestData.get("method").toString().equalsIgnoreCase("delete")==false?response.jsonPath().get():"{}");

//            //查看响应时间 v1.0.1
//            Long responseTimeMs = response.time();
//            System.out.println("Response time in ms using time():"+responseTimeMs);
//            Long responseTimeS = response.timeIn(TimeUnit.SECONDS);
//            System.out.println("Response time in seconds using timeIn():"+responseTimeS);
            //把响应时间加入运行结果 v1.0.1
            responseData.put("responseTimeMs",response.time());
            responseData.put("responseTimeS",response.timeIn(TimeUnit.SECONDS));
            testResultCaseOutputDto.setUserDefinedResponse(responseData.toString());


            testResultCaseOutputDto.setStatus(0);
            //5、获取RestAssured调用响应输出流
            testResultCaseOutputDto.setResponseData(byteArrayOutputStream.toString("utf-8"));
            //重置输出流
            byteArrayOutputStream.reset();
            //6、设置接口调用耗时
            testResultCaseOutputDto.setStartTime(startTime);
            testResultCaseOutputDto.setEndTime(endTime);
            //7、响应提取
            List<TestResultCaseCommonDto> testResultCaseCommonDtos = this.extractData(testCaseEntity,responseData,testRecordEntity);
            testResultCaseOutputDto.setExtracts(testResultCaseCommonDtos);
            //8、对返回值做用例断言
            List<TestResultCaseCommonDto> testResultCaseAssertDtosOfCommon = this.assertResponse(testCaseEntity, responseData);
            testResultCaseOutputDto.setAsserts(testResultCaseAssertDtosOfCommon);
            //9、对返回值做数据库断言
            List<TestResultCaseCommonDto> testResultCaseAssertDtosOfDbCommon = this.dbAssertResponse(testCaseEntity,environmentEntity.getDbConfig());
            testResultCaseOutputDto.setDbAsserts(testResultCaseAssertDtosOfDbCommon);
            //10、根据数测试调用的用例断言、数据库断言设置用例执行结果
            if(testResultCaseAssertDtosOfCommon.stream().filter(s->s.getResult() == null ||s.getResult()==false).count()>0|| testResultCaseAssertDtosOfDbCommon.stream().filter(s->s.getResult() == null ||s.getResult()==false).count()>0){
                testResultCaseOutputDto.setStatus(1);
            }
        }catch (Exception ex){
            Date endTime = new Date();
            testResultCaseOutputDto.setStartTime(startTime);
            testResultCaseOutputDto.setEndTime(endTime);
            testResultCaseOutputDto.setStatus(2);
            testResultCaseOutputDto.setException(ExceptionUtils.getStackTrace(ex));
        }

        return testResultCaseOutputDto;
    }

    /**
     * 随机内容替换，包括替换测试用例的请求数据、返回值等等
     * @param testCaseEntity
     */
    private void randomReplace(InterfaceEntity interfaceEntity, TestCaseEntity testCaseEntity,TestRecordEntity testRecordEntity){
        //需要缓存的数据
        Map<String,Object> cacheValue = new HashMap <>();

        //请求内容替换
        //手机号
        String phone = RandomUtil.randomPhone();
        if(this.hasReplaceKey(interfaceEntity,testCaseEntity, ReplaceUtil.PHONE_KEY)) {
            //替换
            this.replaceTestCase(interfaceEntity, testCaseEntity, ReplaceUtil.PHONE_KEY, phone);
            //添加到需要缓存
            cacheValue.put(ReplaceUtil.PHONE_KEY.replaceAll("##","#"),phone);
        }
        //用户名
        String username = RandomUtil.randomUsername();
        if(this.hasReplaceKey(interfaceEntity,testCaseEntity,ReplaceUtil.USER_NAME_KEY)) {
            //替换
            this.replaceTestCase(interfaceEntity, testCaseEntity, ReplaceUtil.USER_NAME_KEY, username);
            //添加到需要缓存
            cacheValue.put(ReplaceUtil.USER_NAME_KEY.replaceAll("##","#"),username);
        }

        //将替换的随机内容添加到缓存，因为在后续的用例中可以使用到
        if(cacheValue.size()>0) {
            this.addCacheItem(testRecordEntity.getTaskId(), testRecordEntity.getId(), cacheValue);
        }
    }

    /**
     * 全局响应提取替换
     * @param testCaseEntity
     * @param testRecordEntity
     */
    private void globalExtractReplace(InterfaceEntity interfaceEntity, TestCaseEntity testCaseEntity, TestRecordEntity testRecordEntity){
        //获取缓存
        String cacheName = String.format(GLOBAL_CACHE_NAME,testRecordEntity.getTaskId());
        Object result = cacheManager.getCache(cacheName).get(testRecordEntity.getId());
        Map<String,Object> cacheResult =  null ;
        //如果缓存结果不为空，将测试接口、测试用例的相应key替换成缓存中的内容
        if(result!=null) {
            cacheResult = (Map<String,Object>) ((Cache.ValueWrapper) result).get();
            if(cacheResult!=null && cacheResult.size()>0){
                for(String key : cacheResult.keySet()){
                    this.replaceTestCase(interfaceEntity,testCaseEntity, key, cacheResult.get(key).toString());
                }
            }
        }
    }

    /**
     * 测试接口和测试用例中，是否包含指定的key，该key可以是随机Key或响应提取key
     * @param interfaceEntity
     * @param testCaseEntity
     * @param key
     * @return
     */
    private boolean hasReplaceKey(InterfaceEntity interfaceEntity, TestCaseEntity testCaseEntity, String key){
        boolean result = false;
        if(testCaseEntity.getRequestData().indexOf(key)>=0 ||testCaseEntity.getAssertion().indexOf(key)>=0
                || testCaseEntity.getDbAssertion().indexOf(key)>=0 ||interfaceEntity.getPath().indexOf(key)>=0){
            result = true;
        }
        return result;
    }

    /**
     * 测试用例内容替换
     * @param testCaseEntity
     * @param key
     * @param value
     */
    private void replaceTestCase(InterfaceEntity interfaceEntity, TestCaseEntity testCaseEntity, String key, String value){
        //请求内容替换
        testCaseEntity.setRequestData(ReplaceUtil.replace(testCaseEntity.getRequestData(),key,value));
        //用例断言替换
        testCaseEntity.setAssertion(ReplaceUtil.replace(testCaseEntity.getAssertion(),key,value));
        //DB断言内容替换
        testCaseEntity.setDbAssertion(ReplaceUtil.replace(testCaseEntity.getDbAssertion(),key,value));
        //path路径参数替换
        interfaceEntity.setPath(ReplaceUtil.replace(interfaceEntity.getPath(),key,value));
    }

    /**
     * 对返回作响应提取
     * @param testCaseEntity
     * @param responseData
     */
    private List<TestResultCaseCommonDto> extractData(TestCaseEntity testCaseEntity, JSONObject responseData, TestRecordEntity testRecordEntity){
        List<TestResultCaseCommonDto> testResultCaseCommonDtos = new ArrayList <>();
        Boolean extractResult = true;

        //如果当前测试用例有响应提取配置，则进行响应提取
        if(testCaseEntity.getExtract()!=null && testCaseEntity.getExtract().isEmpty() == false) {
            JSONArray extracts = JSONObject.parseArray(testCaseEntity.getExtract());
            if(extracts!=null && extracts.size()>0){
                Map<String,Object> extractCache = new HashMap<>();
                //遍历测试用例配置的响应提取
                for(int i=0;i<extracts.size();i++){
                    //默认提取结果为真
                    extractResult = true;
                    //每个响应结果都保存原有响应提取表达式和响应提取结果
                    TestResultCaseCommonDto testResultCaseCommonDto = new TestResultCaseCommonDto();
                    testResultCaseCommonDto.setAssertExpression(extracts.getJSONArray(i).toJSONString());

                    //处理当前响应提取
                    JSONArray extract = extracts.getJSONArray(i);
                    if(extract!=null && extract.size()>=2) {
                        //响应提取的Key，将作为缓存key
                        String cacheKey = extract.get(0).toString();
                        //响应提取的JsonPath
                        String jsonPath = extract.get(1).toString();
                        Object result = null;
                        try{
                            result = JSONPath.read(responseData.toJSONString(), jsonPath);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        //如果提取到值，则将值根据recordId+参数名的定义存入缓存；否则，表示响应提取失败
                        if(result!=null){
                            if(result instanceof ArrayList){
                                ArrayList arrResult = (ArrayList) result;
                                if(arrResult.size()>0){
                                    extractCache.put(cacheKey,arrResult.get(0));
                                }
                            }else if (result instanceof JSONArray){
                                JSONArray arrResult = (JSONArray) result;
                                if(arrResult.size()>0){
                                    extractCache.put(cacheKey,arrResult.get(0));
                                }
                            }
                        }else{
                            //提取失败
                            extractResult = false;
                        }
                    }

                    testResultCaseCommonDto.setResult(extractResult);
                    testResultCaseCommonDto.setRealValue(JSON.toJSONString(extractCache));

                    testResultCaseCommonDtos.add(testResultCaseCommonDto);
                }
                //将响应提取存到的值添加到当前测试记录缓存
                this.addCacheItem(testRecordEntity.getTaskId(),testRecordEntity.getId(),extractCache);
            }
        }

        return testResultCaseCommonDtos;
    }

    /**
     * 对返回做用例断言
     * @param testCaseEntity
     * @param responseData
     */
    private List<TestResultCaseCommonDto> assertResponse(TestCaseEntity testCaseEntity, JSONObject responseData){
        List<TestResultCaseCommonDto> testResultCaseCommonDtos = new ArrayList <>();

        //如果用例断言配置不为空，则进行用例断言
        if(testCaseEntity.getAssertion()!=null && testCaseEntity.getAssertion().isEmpty() == false) {
            JSONArray assertions = JSONObject.parseArray(testCaseEntity.getAssertion());
            if(assertions!=null && assertions.size()>0){
                for(int i=0;i<assertions.size();i++){
                    JSONArray assertion = assertions.getJSONArray(i);
                    //对每个用例断言进行比较
                    TestResultCaseCommonDto testResultCaseCommonDto = this.assertResponseCompare(assertion,responseData);
                    testResultCaseCommonDtos.add(testResultCaseCommonDto);
                }
            }
        }

        return testResultCaseCommonDtos;
    }

    /**
     * 用例断言比较
     * @param assertion
     * @param responseData
     * @return
     */
    private TestResultCaseCommonDto assertResponseCompare(JSONArray assertion, JSONObject responseData){
        TestResultCaseCommonDto testResultCaseCommonDto = new TestResultCaseCommonDto();
        testResultCaseCommonDto.setAssertExpression(assertion.toJSONString());

        if(assertion!=null && assertion.size()>=3){
            //断言操作类型
            String operator = assertion.get(0).toString();
            //响应用例断言的取值JsonPath
            String jsonPath = assertion.get(1).toString();
            //断言期望值
            String expectValue = assertion.get(2).toString();

            //返回结果中匹配的对象
            Object matchObject = null;
            if(responseData!=null) {
                //从响应数据中，根据JsonPath获取响应对象
                ArrayList matchObjects = null;
                try{
                    Object matchDatas = JSONPath.read(responseData.toJSONString(), jsonPath);
                    if(matchDatas instanceof ArrayList){
                        matchObjects = (ArrayList)matchDatas;
                    }else if(matchDatas instanceof  JSONArray){
                        matchObjects = new ArrayList();
                        matchObjects.add(((JSONArray) matchDatas).get(0).toString());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                if (matchObjects != null && matchObjects.size() > 0) {
                    matchObject = matchObjects.get(0);
                }

                //根据断言操作类型，将响应对象与期望值进行比较
                switch (operator) {
                    case "eq":
                        if (matchObject != null) {
                            boolean assertResult = matchObject.toString().equals(expectValue);

                            testResultCaseCommonDto.setResult(assertResult);
                            if(assertResult==false) {
                                testResultCaseCommonDto.setRealValue(matchObject.toString());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }


        return testResultCaseCommonDto;
    }

    /**
     * 对返回做数据库断言
     * @param testCaseEntity
     * @return
     */
    private List<TestResultCaseCommonDto> dbAssertResponse(TestCaseEntity testCaseEntity, String dbConfig){
        List<TestResultCaseCommonDto> testResultCaseCommonDtos = new ArrayList <>();

        //如果数据库断言配置不为空，则进行数据库断言
        if(testCaseEntity.getDbAssertion()!=null && testCaseEntity.getDbAssertion().isEmpty() == false) {
            JSONArray assertions = JSONObject.parseArray(testCaseEntity.getDbAssertion());
            if(assertions!=null && assertions.size()>0){
                for(int i=0;i<assertions.size();i++){
                    JSONArray assertion = assertions.getJSONArray(i);
                    //对每个数据库断言进行比较
                    TestResultCaseCommonDto testResultCaseCommonDto = this.dbAssertResponseCompare(assertion,dbConfig);
                    testResultCaseCommonDtos.add(testResultCaseCommonDto);
                }
            }
        }

        return testResultCaseCommonDtos;
    }

    /**
     * 数据库断言比较
     * @param assertion
     * @return
     */
    private TestResultCaseCommonDto dbAssertResponseCompare(JSONArray assertion, String dbConfig){
        TestResultCaseCommonDto testResultCaseCommonDto = new TestResultCaseCommonDto();
        testResultCaseCommonDto.setAssertExpression(assertion.toJSONString());

        if(assertion!=null && assertion.size()>=3){
            //断言操作类型
            String operator = assertion.get(0).toString();
            //响应用例断言的取值JsonPath
            String sql = assertion.get(1).toString();
            //断言期望值
            String expectValue = assertion.get(2).toString();

            //动态构造JdbcTemplate，进行数据库访问请求
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            MysqlDataSource dataSource = new MysqlDataSource();
            JSONObject dbConfigData = JSONObject.parseObject(dbConfig);
            dataSource.setURL(dbConfigData.get("host").toString()+"/"+dbConfigData.get("db"));
            dataSource.setUser(dbConfigData.get("user").toString());
            dataSource.setPassword(dbConfigData.get("password").toString());
            jdbcTemplate.setDataSource(dataSource);

            Object dbResult =null;
            switch (operator){
                case "eq":
                    dbResult = jdbcTemplate.queryForObject(sql,Object.class);
                    if(dbResult !=null){
                        boolean assertResult = dbResult.toString().equals(expectValue);
                        testResultCaseCommonDto.setResult(assertResult);
                        if(assertResult == false){
                            testResultCaseCommonDto.setRealValue(dbResult.toString());
                        }
                    }
                    break;
                case "exist":
                    dbResult = jdbcTemplate.queryForObject("select exists (" + sql +")",Object.class);
                    if(dbResult !=null){
                        boolean assertResult = dbResult.toString().equals("1");
                        testResultCaseCommonDto.setResult(assertResult);
                        if(assertResult == false){
                            testResultCaseCommonDto.setRealValue(dbResult.toString());
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return testResultCaseCommonDto;
    }

    /**
     * 添加缓存项目
     * @param taskId
     * @param testRecordId
     * @param cacheValue
     */
    private void addCacheItem(Integer taskId,Integer testRecordId,Map<String,Object> cacheValue){
        //将响应提取存放到当前record的值中
        String cacheName = String.format(GLOBAL_CACHE_NAME,taskId);

        //根据CacheName和Key获取当前测试记录的缓存数据
        Map<String,Object> cacheResult = null;
        Object result = cacheManager.getCache(cacheName).get(testRecordId);
        if(result!=null){
            cacheResult = (Map<String,Object>) ((Cache.ValueWrapper) result).get();
        }
        //如果当前测试记录存在缓存数据，将需要缓存的数据追加到原有缓存数据中；否则，添加缓存数据
        if(cacheResult!=null && cacheResult.size()>0){
            if(cacheValue!=null && cacheValue.size()>0){
                for(String key : cacheValue.keySet()){
                    cacheResult.put(key, cacheValue.get(key));
                }
            }
        }else {
            cacheResult = cacheValue;
        }
        cacheManager.getCache(cacheName).put(testRecordId,cacheResult);
    }
}

