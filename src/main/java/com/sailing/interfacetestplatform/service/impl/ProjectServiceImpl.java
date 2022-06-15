package com.sailing.interfacetestplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import com.sailing.interfacetestplatform.dto.input.project.ProjectCreateInputDto;
import com.sailing.interfacetestplatform.dto.input.project.ProjectUpdateInputDto;
import com.sailing.interfacetestplatform.dto.output.project.ProjectOutputDto;
import com.sailing.interfacetestplatform.entity.ProjectEntity;
import com.sailing.interfacetestplatform.mapper.ProjectMapper;
import com.sailing.interfacetestplatform.service.ProjectService;
import com.sailing.interfacetestplatform.util.SessionUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/15/0015 23:19:10
 * @description:项目服务实现
 **/
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SessionUtil sessionUtil;
    @Override
    public ResponseData<List<ProjectOutputDto>> queryAll() {
        ResponseData<List<ProjectOutputDto>> responseData;

        try {
            List <ProjectEntity> entities = this.list();
            //只取未删除的
//            entities = entities.stream().filter(s->s.getIsDelete() == false).collect(Collectors.toList());
//            List <ProjectOutputDto> outputDtos = entities.stream().sorted(Comparator.comparing(ProjectEntity::getId).reversed()) .map(s -> modelMapper.map(s, ProjectOutputDto.class)).collect(Collectors.toList());
//            entities = entities.stream().filter(s->s.getIsDelete() == false).collect(Collectors.toList());
            List <ProjectOutputDto> outputDtos = entities.stream().filter(s->s.getIsDelete() == false).sorted(Comparator.comparing(ProjectEntity::getId).reversed()) .map(s -> modelMapper.map(s, ProjectOutputDto.class)).collect(Collectors.toList());

            responseData = ResponseData.success(outputDtos);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<ProjectOutputDto> getById(Integer id) {
        ResponseData<ProjectOutputDto> responseData;
        try {
            //验证
            List<String> checkMsgs = new ArrayList<>();
            //ID是否存在
            ProjectEntity entity = super.getById(id);
            if(entity == null){
                checkMsgs.add(String.format("ID为[%s]的数据不存在",id));
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //Entity转DTO
            ProjectOutputDto outputDto = modelMapper.map(entity,ProjectOutputDto.class);

            responseData = ResponseData.success(outputDto);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());

        }

        return responseData;
    }

    @Override
    public ResponseData<ProjectOutputDto> create(ProjectCreateInputDto inputDto) {
        ResponseData<ProjectOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList<>();
            //项目名称是否存在
            QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", inputDto.getName().trim());
            queryWrapper.eq("is_delete", false);
            ProjectEntity projectEntity = this.getOne(queryWrapper, false);
            if (projectEntity != null) {
                checkMsgs.add(String.format("项目[%s]已经存在", inputDto.getName()));
            }
            if (checkMsgs.size() > 0) {
                String checkMsg = checkMsgs.stream().collect(Collectors.joining(","));
                responseData = new ResponseData<>();
                responseData.setCode(1);
                responseData.setMessage(checkMsg);
//                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            ProjectEntity entity = modelMapper.map(inputDto, ProjectEntity.class);
            //设置默认值
            entity.setIsDelete(false);
            //新增
            this.save(entity);
//            //Entity转DTO
//            ProjectOutputDto outputDto = modelMapper.map(entity, ProjectOutputDto.class);
//
//            responseData = ResponseData.success(outputDto);
            responseData = this.getById(entity.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
//            log.error("操作异常：", ex);
//            responseData = ResponseData.failure("操作异常：" + ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<ProjectOutputDto> update(ProjectUpdateInputDto inputDto) {
        ResponseData<ProjectOutputDto> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //验证ID是否已经存在
            ProjectEntity existsEntity = super.getById(inputDto.getId());
            if(existsEntity == null){
                checkMsgs.add(0,String.format("项目ID[%d]不存在",inputDto.getId()));
            }else {
                //项目名称是否存在
                QueryWrapper<ProjectEntity> queryWrapperOfSameName = new QueryWrapper<ProjectEntity>();
                queryWrapperOfSameName.ne("id", inputDto.getId());
                queryWrapperOfSameName.eq("name", inputDto.getName());
                queryWrapperOfSameName.eq("is_delete", false);
                ProjectEntity sameNameProjectEntity = this.getOne(queryWrapperOfSameName, false);
                if (sameNameProjectEntity != null) {
                    checkMsgs.add(String.format("项目[%s]已经存在", inputDto.getName()));
                }
                //修改人是否为项目的创建人和负责人
                QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", inputDto.getId());
                queryWrapper.eq("is_delete", false);
                ProjectEntity projectEntity = this.getOne(queryWrapper, false);
                if (projectEntity != null) {
                    //只能修改自己创建或负责的项目
                    SessionUtil.CurrentUser currentUser = sessionUtil.getCurrentUser();
                    if (projectEntity.getCreateById().intValue() != currentUser.getUserEntity().getId().intValue() && projectEntity.getLeaderId().intValue() != currentUser.getUserEntity().getId().intValue()) {
                        checkMsgs.add(String.format("项目[%s]的创建人和负责人才能修改该项目", projectEntity.getName()));
                    }
                } else {
                    checkMsgs.add("项目不存在或已经删除");
                }
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //DTO转Entity
            ProjectEntity entity = modelMapper.map(inputDto,ProjectEntity.class);
            //新增
            this.updateById(entity);
//            //Entity转DTO
//            ProjectOutputDto outputDto = modelMapper.map(entity,ProjectOutputDto.class);
//
//            responseData = ResponseData.success(outputDto);
            responseData = this.getById(entity.getId());
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }

    @Override
    public ResponseData<Boolean> delete(Integer id) {
        ResponseData<Boolean> responseData;

        try {
            //数据验证
            List<String> checkMsgs = new ArrayList <>();
            //项目名称是否存在
            QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            queryWrapper.eq("is_delete",false);
            ProjectEntity projectEntity = this.getOne(queryWrapper,false);
            if (projectEntity!=null) {
                //只能删除自己创建或负责的项目
                SessionUtil.CurrentUser currentUser = sessionUtil.getCurrentUser();
                if (projectEntity.getCreateById().intValue() != currentUser.getUserEntity().getId().intValue() && projectEntity.getLeaderId().intValue() != currentUser.getUserEntity().getId().intValue()) {
                    checkMsgs.add(String.format("项目[%s]的创建人和负责人才能删除该项目", projectEntity.getName()));
                }
            }else {
                checkMsgs.add("项目不存在或已经删除");
            }
            if(checkMsgs.size()>0){
                responseData = new ResponseData <>();
                responseData.setCode(1);
                responseData.setMessage(checkMsgs.stream().collect(Collectors.joining(",")));

                return responseData;
            }

            //修改状态
            projectEntity.setIsDelete(true);
            Boolean result = this.updateById(projectEntity);

            responseData = ResponseData.success(result);
        }catch (Exception ex){
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.toString());
//            log.error("操作异常：",ex);
//            responseData = ResponseData.failure("操作异常："+ex.toString());
        }

        return responseData;
    }
}
