package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.module.ModuleCreateInputDto;
import com.sailing.interfacetestplatform.entity.ModuleEntity;
import com.sailing.interfacetestplatform.mapper.ModuleMapper;
import com.sailing.interfacetestplatform.service.ModuleService;
import com.sailing.interfacetestplatform.service.ModulesSynchronousService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/9/30 11:43
 * @description:
 **/
@Service
public class ModulesSynchronousServiceImpl extends ServiceImpl<ModuleMapper, ModuleEntity> implements ModulesSynchronousService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ModuleService moduleService;

//    @Autowired
//    CacheManager cacheManager;
//    @Autowired
//    SessionUtil sessionUtil;
//    @Autowired
//    ProjectService projectService;
//    @Autowired
//    EnvironmentService environmentService;

//    @Autowired
//    InterfaceService interfaceService;
//    @Autowired
//    @Lazy
//    TestCaseService testCaseService;
//    @Autowired
//    @Lazy
//    TestRecordService testRecordService;
//    @Autowired
//    @Lazy
//    TestSuitService testSuitService;
//    //
//    @Autowired
//    @Lazy
//    TaskModuleService taskModuleService;
//    @Autowired
//    @Lazy
//    TestReportService reportService;
//    @Autowired
//    @Lazy
//    TaskTestService taskTestService;
    //循环取出传入的模块名列表中的模块名 是否  和项目is_delete=0状态下的模块名 有重复；不重复就添加此模块到数据库中，重复的话就继续循环。
    @Override
    public ResponseData<Boolean> addSwaggerModules(List<String> modulesName) {
        //1.获取所有模块详情，只获取is_delete=0的模块
        QueryWrapper<ModuleEntity> moduleEntityQueryWrapper = new QueryWrapper<>();
        moduleEntityQueryWrapper.eq("is_delete",false);
        List<ModuleEntity> moduleEntities = moduleService.list(moduleEntityQueryWrapper);
        List<String> nonexistentModulesName=new ArrayList<String> ();
        for(int i=0;i<modulesName.size();i++){
            //modulesName[i]
            //2.根据modulesName[i]模块名查询moduleEntities是否有同名的name,把不存在的模块名存到列表nonexistentModulesName中
            //todo 需要和前端一起处理，项目id的传入,查询本项目下不重复的模块名，目前是查所有项目下的
            final int tmp = i;
            List<ModuleEntity> repeatedModuleEntities = moduleEntities.stream().filter(t-> modulesName.get(tmp).contains(t.getName())).collect(Collectors.toList());
            if(repeatedModuleEntities.size()==0){
                nonexistentModulesName.add(modulesName.get(i));
            }

        }
        System.out.println("项目不存在的模块列表为："+nonexistentModulesName);
        //3.把不存在的模块列表里的模块保存到模块表中，保存的项目id默认设置91
        //todo 需要和前端一起处理，项目id的传入
        for(int i=0;i<nonexistentModulesName.size();i++) {
            ModuleCreateInputDto moduleCreateInputDto = new ModuleCreateInputDto();
            moduleCreateInputDto.setProjectId(91);
            moduleCreateInputDto.setName(nonexistentModulesName.get(i));
            moduleCreateInputDto.setDescription("");
            moduleService.create(moduleCreateInputDto);
        }
        return null;
    }

}
