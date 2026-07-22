package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ustb.dao.TaskSummaryMapper;
import com.ustb.entity.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSummaryService {

    @Autowired
    private TaskSummaryMapper taskSummaryMapper;

    public int add(TaskSummary taskSummary) {
        return taskSummaryMapper.insert(taskSummary);
    }

    public int update(TaskSummary taskSummary) {
        return taskSummaryMapper.updateById(taskSummary);
    }

    public int deleteById(Long id) {
        return taskSummaryMapper.deleteById(id);
    }

    public TaskSummary findById(Long id) {
        return taskSummaryMapper.selectById(id);
    }

    public List<TaskSummary> findList() {
        Wrapper<TaskSummary> wrapper = new QueryWrapper<>();
        return taskSummaryMapper.selectList(wrapper);
    }
}