package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.TaskInfo;
import com.ustb.service.AIService;
import com.ustb.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AI 控制器 - 提供任务拆解相关的 AI 接口
 */
@Controller
@RequestMapping("/ai/")
@CrossOrigin
public class AIController {

    @Autowired
    private AIService aiService;

    @Autowired
    private TaskInfoService taskInfoService;

    /**
     * 单任务 AI 建议（文档 6.2）
     * 请求体: { projectName, taskTitle, taskDescription }
     * 响应: { suggestion: "建议文本" }
     */
    @PostMapping("task-suggestion")
    @ResponseBody
    public Result taskSuggestion(@RequestBody Map<String, Object> body) {
        System.out.println("AI taskSuggestion() 方法被执行了");
        System.out.println("请求参数: " + body);

        String projectName = (String) body.getOrDefault("projectName", "");
        String taskTitle = (String) body.getOrDefault("taskTitle", "");
        String taskDescription = (String) body.getOrDefault("taskDescription", "");

        if (taskTitle.isEmpty()) {
            return new Result(101, "任务标题不能为空");
        }

        String suggestion = aiService.generateTaskSuggestion(projectName, taskTitle, taskDescription);

        if (suggestion == null || suggestion.isEmpty()) {
            suggestion = "建议分三步执行：1）明确任务边界和交付标准；2）制定执行计划并分配资源；3）按计划推进并定期检查进度。注意识别潜在风险，提前制定应对方案。";
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("suggestion", suggestion);

        return new Result(100, "建议生成成功", resultData);
    }

    /**
     * AI 任务拆解
     * 请求体: { projectId, operatorId, projectName, goal, description, members }
     * 响应: { summary, items: [{ title, description, priority, suggestedDays, assigneeId }] }
     */
    @PostMapping("task-plan")
    @ResponseBody
    public Result taskPlan(@RequestBody Map<String, Object> body) {
        System.out.println("AI taskPlan() 方法被执行了");
        System.out.println("请求参数: " + body);

        String projectName = (String) body.getOrDefault("projectName", "");
        String goal = (String) body.getOrDefault("goal", "");
        String description = (String) body.getOrDefault("description", "");

        // 获取成员列表
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> members = (List<Map<String, Object>>) body.get("members");
        if (members == null || members.isEmpty()) {
            // 如果没有传入成员，创建一个默认成员
            members = new ArrayList<>();
            Map<String, Object> defaultMember = new HashMap<>();
            defaultMember.put("id", body.getOrDefault("operatorId", 1));
            defaultMember.put("realName", "当前用户");
            defaultMember.put("role", "负责人");
            members.add(defaultMember);
        }

        // 调用 AI 服务
        Map<String, Object> planResult = aiService.generateTaskPlan(projectName, goal, description, members);

        // AI 不可用时使用兜底方案
        if (planResult == null || planResult.get("items") == null
                || ((List<?>) planResult.get("items")).isEmpty()) {
            System.out.println("AI 不可用，使用标准拆解方案");
            planResult = aiService.buildFallbackPlan(goal, members);
        }

        return new Result(100, "任务拆解成功", planResult);
    }

    /**
     * 导入 AI 生成的任务到数据库
     * 请求体: { projectId, creatorId, items: [{ title, description, priority, suggestedDays, assigneeId }] }
     */
    @PostMapping("task-plan/import")
    @ResponseBody
    public Result taskPlanImport(@RequestBody Map<String, Object> body) {
        System.out.println("AI taskPlanImport() 方法被执行了");
        System.out.println("请求参数: " + body);

        Object projectIdObj = body.get("projectId");
        Object creatorIdObj = body.get("creatorId");
        Long projectId = projectIdObj instanceof Number ? ((Number) projectIdObj).longValue() : null;
        Long creatorId = creatorIdObj instanceof Number ? ((Number) creatorIdObj).longValue() : null;

        if (projectId == null || creatorId == null) {
            return new Result(101, "项目ID或创建人ID不能为空");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        if (items == null || items.isEmpty()) {
            return new Result(101, "任务列表不能为空");
        }

        int importedCount = 0;
        for (Map<String, Object> item : items) {
            TaskInfo task = new TaskInfo();
            task.setProjectId(projectId);
            task.setCreatorId(creatorId);
            task.setTitle((String) item.getOrDefault("title", "未命名任务"));
            task.setDescription((String) item.getOrDefault("description", ""));
            task.setPriority((String) item.getOrDefault("priority", "中"));
            task.setStatus("未开始");

            // 负责人
            Object assigneeIdObj = item.get("assigneeId");
            if (assigneeIdObj instanceof Number) {
                task.setAssigneeId(((Number) assigneeIdObj).longValue());
            }

            // 截止日期：根据建议天数计算
            Object suggestedDaysObj = item.get("suggestedDays");
            int suggestedDays = suggestedDaysObj instanceof Number ? ((Number) suggestedDaysObj).intValue() : 3;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, suggestedDays);
            task.setDueDate(cal.getTime());

            task.setCreateTime(new Date());

            if (taskInfoService.add(task) > 0) {
                importedCount++;
            }
        }

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("importedCount", importedCount);
        resultData.put("totalCount", items.size());

        return new Result(100, "成功导入 " + importedCount + " 个任务", resultData);
    }
}
