package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.TaskLogMapper;
import com.ustb.entity.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskLogService {
    @Autowired
    private TaskLogMapper taskLogMapper;

    public int add(TaskLog taskLog) {
        return taskLogMapper.insert(taskLog);
    }

    public List<TaskLog> findList(Long taskId) {
        QueryWrapper<TaskLog> wrapper = new QueryWrapper<>();
        if (taskId != null) {
            wrapper.eq("task_id", taskId);
        }
        wrapper.orderByDesc("create_time");
        return taskLogMapper.selectList(wrapper);
    }
}
