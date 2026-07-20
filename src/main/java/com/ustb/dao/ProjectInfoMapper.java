package com.ustb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ustb.entity.ProjectInfo;
import org.apache.ibatis.annotations.Mapper;
/*
Mapper层负责执行数据库操作
MyBatis-Plus Mapper接口
MyBatis-Plus默认自动实现了CRUD操作
如果当前提供的方法无法满足用户有的要求，同时支持Sql语句方式
 */
@Mapper
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {
}
