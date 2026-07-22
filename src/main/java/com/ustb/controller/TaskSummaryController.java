package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.TaskSummary;
import com.ustb.service.TaskSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/summary/")
@CrossOrigin
public class TaskSummaryController {

    @Autowired
    private TaskSummaryService taskSummaryService;

    @GetMapping("list")
    @ResponseBody
    public Result list(@RequestParam(name = "currentUserId", required = false) Long currentUserId) {
        List<TaskSummary> list = taskSummaryService.findList();
        return new Result(100, "获取成功", list);
    }

    @PostMapping("add")
    @ResponseBody
    public Result add(@RequestBody Map<String, Object> body) {
        Long currentUserId = body.get("currentUserId") instanceof Number ? ((Number) body.get("currentUserId")).longValue() : null;
        if (currentUserId == null) {
            return new Result(101, "未登录或用户ID丢失");
        }

        TaskSummary summary = new TaskSummary();
        summary.setProjectId(Long.valueOf(String.valueOf(body.get("projectId"))));
        if (body.get("taskId") != null && !body.get("taskId").toString().isEmpty()) {
            summary.setTaskId(Long.valueOf(String.valueOf(body.get("taskId"))));
        }
        summary.setSummaryType((String) body.get("summaryType"));
        summary.setContent((String) body.get("content"));
        summary.setCreatedBy(currentUserId);
        summary.setCreateTime(new Date());

        if (taskSummaryService.add(summary) > 0) {
            return new Result(100, "总结创建成功");
        }
        return new Result(101, "总结创建失败");
    }

    @PutMapping("update")
    @ResponseBody
    public Result update(@RequestBody Map<String, Object> body) {
        Object idObj = body.get("id");
        if (idObj == null) {
            return new Result(101, "总结ID不能为空");
        }
        Long id = Long.valueOf(String.valueOf(idObj));

        TaskSummary summary = taskSummaryService.findById(id);
        if (summary == null) {
            return new Result(101, "总结不存在");
        }

        if (body.get("projectId") != null) {
            summary.setProjectId(Long.valueOf(String.valueOf(body.get("projectId"))));
        }
        if (body.get("taskId") != null && !body.get("taskId").toString().isEmpty()) {
            summary.setTaskId(Long.valueOf(String.valueOf(body.get("taskId"))));
        } else if (body.containsKey("taskId")) {
            summary.setTaskId(null);
        }
        if (body.get("summaryType") != null) {
            summary.setSummaryType((String) body.get("summaryType"));
        }
        if (body.get("content") != null) {
            summary.setContent((String) body.get("content"));
        }

        if (taskSummaryService.update(summary) > 0) {
            return new Result(100, "总结更新成功");
        }
        return new Result(101, "总结更新失败");
    }

    @DeleteMapping("delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Long id) {
        if (taskSummaryService.deleteById(id) > 0) {
            return new Result(100, "总结删除成功");
        }
        return new Result(101, "总结删除失败");
    }
}