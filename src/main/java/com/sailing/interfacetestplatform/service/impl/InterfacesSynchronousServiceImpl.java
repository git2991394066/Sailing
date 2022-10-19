package com.sailing.interfacetestplatform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.interf.InterfaceCreateInputDto;
import com.sailing.interfacetestplatform.entity.InterfaceEntity;
import com.sailing.interfacetestplatform.entity.ModuleEntity;
import com.sailing.interfacetestplatform.mapper.InterfaceMapper;
import com.sailing.interfacetestplatform.service.InterfaceService;
import com.sailing.interfacetestplatform.service.InterfacesSynchronousService;
import com.sailing.interfacetestplatform.service.ModuleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/30 16:37
 * @description:
 **/
@Service
public class InterfacesSynchronousServiceImpl extends ServiceImpl<InterfaceMapper, InterfaceEntity> implements InterfacesSynchronousService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    InterfaceService interfaceService;
    @Autowired
    ModuleService moduleService;
//    @Autowired
//    ProjectService projectService;
//    @Autowired
//    @Lazy
//    TestCaseService testCaseService;

    @Override
    public ResponseData<Boolean> addSwaggerInterfaces(JSONObject paths, List<String> urls,Integer projectId) {
        //1.获取数据库中接口表中，符合is_delete=0和projectid等于请求接口传入的projectId 条件 的接口列表
        QueryWrapper<InterfaceEntity> interfaceEntityQueryWrapper = new QueryWrapper<>();
        interfaceEntityQueryWrapper.eq("project_id",projectId);
        interfaceEntityQueryWrapper.eq("is_delete",false);
        //数据库中同项目未被删除 已经存在的接口
        List<InterfaceEntity> interfaceEntities = interfaceService.list(interfaceEntityQueryWrapper);

        //把数据库中不存在url+请求方式接口记录 的统计在列表中
        List<String> nonexistentUrlCombineMethod=new ArrayList<String> ();

        //1.1用于通过提取到的模块名查找到模块id，获取所有模块详情，只获取is_delete=0的模块和筛选projectid
        QueryWrapper<ModuleEntity> moduleEntityQueryWrapper = new QueryWrapper<>();
        moduleEntityQueryWrapper.eq("project_id",projectId);
        moduleEntityQueryWrapper.eq("is_delete",false);
        List<ModuleEntity> moduleEntities = moduleService.list(moduleEntityQueryWrapper);

//        2.遍历url，解析paths 获取每个url下的请求集合
        for(int i=0;i<urls.size();i++){
            //获取这个url下的请求集合，头部是请求方式
            JSONObject requestAndResponse=  paths.getJSONObject(urls.get(i));
            final int tmp1 = i;
            List<InterfaceEntity> repeatedUrlEntities = interfaceEntities.stream().filter(t-> t.getPath().equals(urls.get(tmp1))).collect(Collectors.toList());

            //        3.遍历该url下的请求方式 判断第1步得到的接口列表中   是否存在同url和请求方式的接口记录信息
            //获取此url下的请求方式列表
            List<String> methods=new ArrayList<String>();
            methods=getJsonFirstFloorKey(requestAndResponse);
            List<String> finalMethods = methods;

            for(int j=0;j<methods.size();j++){
                final int tmp2 = j;
                List<InterfaceEntity> repeatedInterfaceEntities = repeatedUrlEntities.stream().filter(t-> t.getRequestMethod().equals(finalMethods.get(tmp2))).collect(Collectors.toList());
                if(repeatedInterfaceEntities.size()==0){
                    //        4.如果不存在则往接口表中添加url+请求方式的接口数据,如果存在则继续遍历此url的请求方式
                    InterfaceCreateInputDto interfaceCreateInputDto = new InterfaceCreateInputDto();
                    interfaceCreateInputDto.setProjectId(projectId);
                    interfaceCreateInputDto.setDescription("");
                    interfaceCreateInputDto.setRequestMethod(finalMethods.get(j));
                    interfaceCreateInputDto.setPath(urls.get(i));
                    //处理接口名称
                    //获取请求方法中的请求响应信息
                    JSONObject requestAndResponseInMethods=  requestAndResponse.getJSONObject(methods.get(j));
                    //接口名称
                    String interfaceName = requestAndResponseInMethods.getString("summary");
                    if(interfaceName==null){
                        interfaceName="空的接口名"+System.currentTimeMillis();
                    }
                    //System.out.println("接口名称为"+interfaceName);
                    interfaceCreateInputDto.setName(interfaceName);
                    //处理模块id，通过提取的模块名查询到对应模块id
                    //提取模块名
                    JSONArray tags = requestAndResponseInMethods.getJSONArray("tags");//返回结果是列表
                    List<String> moduleName=new ArrayList<>();
                    moduleName.add((String) tags.get(0));
                    //提取到的模块详情信息,获取关联的模块id
                    List<ModuleEntity> moduleEntities1 = moduleEntities.stream().filter(t-> moduleName.get(0).contains(t.getName())).collect(Collectors.toList());
                    Integer moduleId=moduleEntities1.get(0).getId();
                    //System.out.println("模块名为"+(String) tags.get(0)+"模块id为"+moduleId);
                    interfaceCreateInputDto.setModuleId(moduleId);
                    interfaceCreateInputDto.setResponseType("JSON");
                    interfaceService.create(interfaceCreateInputDto);
                    //记录项目不存在的url+请求方式
                    nonexistentUrlCombineMethod.add(finalMethods.get(j));
                    nonexistentUrlCombineMethod.add(urls.get(i));
                }


            }

        }
        //        4.如果存在则继续遍历此url的请求方式，如果不存在则往接口表中添加url+请求方式的接口数据
        System.out.println("projectId为"+projectId+"的项目不存在的url+请求方式列表为："+nonexistentUrlCombineMethod);



//
//        List<JSONObject> allMethods=new ArrayList<JSONObject>();
//        List<JSONObject> getRequests=new ArrayList<JSONObject>();
//        List<JSONObject> deleteRequests=new ArrayList<JSONObject>();
//        List<JSONObject> postRequests=new ArrayList<JSONObject>();
//        List<JSONObject> putRequests=new ArrayList<JSONObject>();

//        //遍历urls,获取key=urls[i]的value（包含请求方法及其下面的默认请求体）下的，把其中的get/delete/post/put请求都分类处理
//        for(int i=0;i<urls.size();i++){
//            //将json字符串转化为json对象
//            JSONObject jsonRequestMethods = paths.getJSONObject(urls.get(i));
//            allMethods.add(jsonRequestMethods);
//            if(jsonRequestMethods.getJSONObject("get") instanceof JSONObject){
//                getRequests.add(jsonRequestMethods);
//
//            } else if (jsonRequestMethods.getJSONObject("delete") instanceof JSONObject) {
//                deleteRequests.add(jsonRequestMethods);
//
//            }else if (jsonRequestMethods.getJSONObject("post") instanceof JSONObject) {
//                postRequests.add(jsonRequestMethods);
//
//            }else if (jsonRequestMethods.getJSONObject("put") instanceof JSONObject) {
//                putRequests.add(jsonRequestMethods);
//
//            }
//        }
////        System.out.println("allMethods:"+allMethods);
//        System.out.println("getRequests:"+getRequests);
//        System.out.println("deleteRequests"+deleteRequests);
////        System.out.println("postRequests"+postRequests);
////        System.out.println("putRequests"+putRequests);
        return null;
    }
//    public static Boolean addGetRequests(List<JSONObject> getRequests){
//
//        for(int i=0;i<getRequests.size();i++){
//            String interfanceName=getRequests.get(i).getJSONObject("get").getString("summary");
//            String interfanceModuleName= getRequests.get(i).getJSONObject("get").getString("tags");
//            JSONArray interfanceParameters=getRequests.get(i).getJSONObject("get").getJSONArray("parameters");
//        }
//        return true;
//    }
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
