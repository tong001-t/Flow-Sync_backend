package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.ProjectInfoMapper;
import com.ustb.entity.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*
Service层：也称为业务员层
@Service:表示当前类是Service层代码
 */
@Service
public class ProjectInfoService {
    //通过自动装配方式创建Mapper对象
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    //使用Mapper
    /*
    1.新增项目信息到数据库
     */
    public int add(ProjectInfo projectInfo) {
        return projectInfoMapper.insert(projectInfo);
    }
    /*
    2.按Id更新项目信息
     */
    public int updateById(ProjectInfo projectInfo) {
        return projectInfoMapper.updateById(projectInfo);
    }
    /*
    3.按Id删除项目信息
     */
    public int delById(long id) {
        return projectInfoMapper.deleteById(id);
    }
    /*
    4.按Id查询项目信息
     */
    public ProjectInfo findById(long id) {
        return projectInfoMapper.selectById(id);
    }
    /*
    5.按Id返回项目列表
    list()
     */
    public List<ProjectInfo> findList() {
        //Wrapper对象中包含了查询参数集合
        Wrapper<ProjectInfo> wrapper=new QueryWrapper<>();
        return projectInfoMapper.selectList(wrapper);
    }
}
