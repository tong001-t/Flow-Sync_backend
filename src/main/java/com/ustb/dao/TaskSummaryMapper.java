package com.ustb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ustb.entity.TaskSummary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskSummaryMapper extends BaseMapper<TaskSummary> {
}