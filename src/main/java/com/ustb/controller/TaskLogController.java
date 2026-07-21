package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.TaskLog;
import com.ustb.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task-log/")
@CrossOrigin
public class TaskLogController {
    @Autowired
    private TaskLogService taskLogService;

    @GetMapping("list")
    @ResponseBody
    public Result list(@RequestParam(value = "taskId", required = false) Long taskId) {
        List<TaskLog> list = taskLogService.findList(taskId);
        return new Result(100, list);
    }

    @PostMapping("add")
    @ResponseBody
    public Result add(@RequestBody Map<String, Object> params) {
        TaskLog log = new TaskLog();
        log.setTaskId(Long.valueOf(params.get("taskId").toString()));
        log.setProgressPercent(Integer.valueOf(params.get("progressPercent").toString()));
        log.setContent((String) params.get("content"));

        Object currentUserId = params.get("currentUserId");
        if (currentUserId != null) {
            log.setOperatorId(Long.valueOf(currentUserId.toString()));
        }
        log.setCreateTime(new Date());

        if (taskLogService.add(log) > 0) {
            return new Result(100, "添加成功");
        } else {
            return new Result(101, "添加失败");
        }
    }
}
