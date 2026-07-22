package com.ustb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:Wangtao
 * @Data:2026/7/22 14:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Overview {
    // 统计数据
    private Long memberCount;
    private Long projectCount;
    private Long taskCount;
    private Long summaryCount;

    // 最近项目列表 (包含项目名称、状态、优先级)
    private List<ProjectInfo> recentProjects;

    // 最近任务列表 (包含任务标题、状态、优先级)
    private List<TaskInfo> recentTasks;

    //get/set访问器

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public Long getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Long projectCount) {
        this.projectCount = projectCount;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public Long getSummaryCount() {
        return summaryCount;
    }

    public void setSummaryCount(Long summaryCount) {
        this.summaryCount = summaryCount;
    }

    public List<ProjectInfo> getRecentProjects() {
        return recentProjects;
    }

    public void setRecentProjects(List<ProjectInfo> recentProjects) {
        this.recentProjects = recentProjects;
    }

    public List<TaskInfo> getRecentTasks() {
        return recentTasks;
    }

    public void setRecentTasks(List<TaskInfo> recentTasks) {
        this.recentTasks = recentTasks;
    }
}
