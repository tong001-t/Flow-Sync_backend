package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.TaskInfoMapper;
import com.ustb.entity.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Wangtao
 * @Data:2026/7/20 19:47
 */
@Service
public class TaskInfoService {
    @Autowired
    private TaskInfoMapper taskInfoMapper;

    public int add(TaskInfo taskInfo) {
        return taskInfoMapper.insert(taskInfo);
    }

    public int delById(Long id) {
        return taskInfoMapper.deleteById(id);
    }

    public int update(TaskInfo taskInfo) {
        return taskInfoMapper.updateById(taskInfo);
    }

    public TaskInfo findById(Long id) {
        return taskInfoMapper.selectById(id);
    }

    public List<TaskInfo> findList() {
        Wrapper<TaskInfo> wrapper = new QueryWrapper<>();
        return taskInfoMapper.selectList(wrapper);
    }

    // 2. 带参：查询指定项目的下级任务
    public List<TaskInfo> findList(Long projectId) {
        QueryWrapper<TaskInfo> wrapper = new QueryWrapper<>();
        // 加上 project_id 等于 projectId 的条件
        wrapper.eq("project_id", projectId);
        return taskInfoMapper.selectList(wrapper);
    }

    /**
     * 数据隔离：只返回指定用户负责的任务
     */
    public List<TaskInfo> findListByAssignee(Long assigneeId) {
        QueryWrapper<TaskInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("assignee_id", assigneeId);
        return taskInfoMapper.selectList(wrapper);
    }
}
