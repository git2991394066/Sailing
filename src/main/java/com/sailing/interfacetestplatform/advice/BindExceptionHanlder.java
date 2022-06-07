package com.sailing.interfacetestplatform.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sailing.interfacetestplatform.dto.common.ResponseData;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/7/0007 23:59:30
 * @description:控制器通用异常拦截
 **/
@RestControllerAdvice
public class BindExceptionHanlder {
    /**
     * 拦截BindException，针对使用Spring Validation进行注解进行“通用规则验证”的输入参数，会抛出此异常
     * 针对此类异常，给调用方响应与正常返回相同的响应数据，但Http状态码和业务编码为自定义的996
     * @param ex
     * @param response
     * @throws IOException
     */
    @ExceptionHandler(BindException.class)
    public void handleBindException(BindException ex, HttpServletResponse response) throws IOException {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<String> errors = new ArrayList<>();
        fieldErrors.stream().forEach(s->{
//            errors.add(s.getField()+s.getDefaultMessage());
            errors.add(s.getDefaultMessage());
        });

        ResponseData responseData = new ResponseData();
        responseData.setCode(996);
        responseData.setMessage(errors.stream().collect(Collectors.joining(",")));
        String json = new ObjectMapper().writeValueAsString(responseData);
        response.setStatus(996);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
