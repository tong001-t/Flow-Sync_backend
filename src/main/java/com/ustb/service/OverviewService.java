package com.ustb.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ustb.dao.ProjectInfoMapper;
import com.ustb.dao.TaskInfoMapper;
import com.ustb.dao.TaskSummaryMapper;
import com.ustb.dao.UserInfoMapper;
import com.ustb.entity.Overview;
import com.ustb.entity.ProjectInfo;
import com.ustb.entity.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Wangtao
 * @Data:2026/7/22 14:23
 */
@Service
public class OverviewService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private TaskSummaryMapper taskSummaryMapper;

    public Overview getOverviewData() {
        Overview vo = new Overview();

        // 1. 查询各表总数量
        vo.setMemberCount(userInfoMapper.selectCount(null));
        vo.setProjectCount(projectInfoMapper.selectCount(null));
        vo.setTaskCount(taskInfoMapper.selectCount(null));
        vo.setSummaryCount(taskSummaryMapper.selectCount(null));

        // 2. 查询最近 5 条项目列表（按创建时间/ID降序）
        LambdaQueryWrapper<ProjectInfo> projectWrapper = new LambdaQueryWrapper<>();
        // 如果你的 ProjectInfo 有 id 或 createTime，可以按 id 降序查最新 5 条：
        projectWrapper.orderByDesc(ProjectInfo::getId).last("LIMIT 5");
        List<ProjectInfo> recentProjects = projectInfoMapper.selectList(projectWrapper);
        vo.setRecentProjects(recentProjects);

        // 3. 查询最近 5 条任务列表（按创建时间/ID降序）
        LambdaQueryWrapper<TaskInfo> taskWrapper = new LambdaQueryWrapper<>();
        taskWrapper.orderByDesc(TaskInfo::getId).last("LIMIT 5");
        List<TaskInfo> recentTasks = taskInfoMapper.selectList(taskWrapper);
        vo.setRecentTasks(recentTasks);

        return vo;
    }
}
