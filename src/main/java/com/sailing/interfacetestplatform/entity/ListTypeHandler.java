package com.sailing.interfacetestplatform.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @auther:张启航Sailling
 * @createDate:2022/6/8/0008 16:33:06
 * @description:List转JDBC字符串工具类
 **/
@Slf4j
//JDBC数据类型
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})          // java数据类型
public class ListTypeHandler implements TypeHandler<List<String>>{
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        String hobbys=dealListToOneStr(strings);
        preparedStatement.setString(i,hobbys);

    }
    /**
     * 集合拼接字符串
     *
     * @param parameter
     * @return
     */
    private String dealListToOneStr(List <String> parameter) {
        if(parameter==null || parameter.size()<=0){
            return null;
        }
        String res="";
        for(int i=0;i<parameter.size();i++){
            if(i==parameter.size()-1){
                res += parameter.get(i);
                return res;
            }
            res+=parameter.get(i)+",";
        }
        return null;
    }

    @Override
    public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
        return Arrays.asList(resultSet.getString(s).split(","));
    }

    @Override
    public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
        return Arrays.asList(resultSet.getString(i).split(","));
    }

    @Override
    public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
//        String hobbys=callableStatement.getString(i);
//        return Arrays.asList(hobbys.split(","));
        return Arrays.asList(callableStatement.getString(i).split(","));

    }
}
