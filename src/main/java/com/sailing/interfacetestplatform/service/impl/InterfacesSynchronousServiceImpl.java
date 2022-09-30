package com.sailing.interfacetestplatform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.entity.InterfaceEntity;
import com.sailing.interfacetestplatform.mapper.InterfaceMapper;
import com.sailing.interfacetestplatform.service.InterfacesSynchronousService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/30 16:37
 * @description:
 **/
@Service
public class InterfacesSynchronousServiceImpl extends ServiceImpl<InterfaceMapper, InterfaceEntity> implements InterfacesSynchronousService {
    @Autowired
    ModelMapper modelMapper;
//    @Autowired
//    ModuleService moduleService;
//    @Autowired
//    ProjectService projectService;
//    @Autowired
//    @Lazy
//    TestCaseService testCaseService;

    @Override
    public ResponseData<Boolean> addSwaggerInterfaces(JSONObject paths, List<String> urls,Integer projectId) {
        List<JSONObject> allMethods=new ArrayList<JSONObject>();
        List<JSONObject> getRequests=new ArrayList<JSONObject>();
        List<JSONObject> deleteRequests=new ArrayList<JSONObject>();
        List<JSONObject> postRequests=new ArrayList<JSONObject>();
        List<JSONObject> putRequests=new ArrayList<JSONObject>();

        //遍历urls,获取key=urls[i]的value（包含请求方法及其下面的默认请求体）下的，把其中的get/delete/post/put请求都分类处理
        for(int i=0;i<urls.size();i++){
            //将json字符串转化为json对象
            JSONObject jsonRequestMethods = paths.getJSONObject(urls.get(i));
            allMethods.add(jsonRequestMethods);
            if(jsonRequestMethods.getJSONObject("get") instanceof JSONObject){
                getRequests.add(jsonRequestMethods);
                

            } else if (jsonRequestMethods.getJSONObject("delete") instanceof JSONObject) {
                deleteRequests.add(jsonRequestMethods);

            }else if (jsonRequestMethods.getJSONObject("post") instanceof JSONObject) {
                postRequests.add(jsonRequestMethods);

            }else if (jsonRequestMethods.getJSONObject("put") instanceof JSONObject) {
                putRequests.add(jsonRequestMethods);

            }
        }
//        System.out.println("allMethods:"+allMethods);
        System.out.println("getRequests:"+getRequests);
        System.out.println("deleteRequests"+deleteRequests);
//        System.out.println("postRequests"+postRequests);
//        System.out.println("putRequests"+putRequests);
        return null;
    }
    public static Boolean addGetRequests(List<JSONObject> getRequests){

        for(int i=0;i<getRequests.size();i++){
            String interfanceName=getRequests.get(i).getJSONObject("get").getString("summary");
            String interfanceModuleName= getRequests.get(i).getJSONObject("get").getString("tags");
            JSONArray interfanceParameters=getRequests.get(i).getJSONObject("get").getJSONArray("parameters");
        }
        return true;
    }
}
