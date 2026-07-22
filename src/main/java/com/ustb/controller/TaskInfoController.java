package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.TaskInfo;
import com.ustb.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:Wangtao
 * @Data:2026/7/20 21:20
 */
@Controller
@RequestMapping("/task/")
@CrossOrigin
public class TaskInfoController {
    @Autowired
    private TaskInfoService taskInfoService;

    @PostMapping("add")
    @ResponseBody
    public Result add(@RequestBody TaskInfo taskInfo) {
        if(taskInfoService.add(taskInfo)>0)
            return new Result(100,"任务信息添加成功");
        else
            return new Result(101,"任务信息添加失败");
    }

    @PutMapping("update")
    @ResponseBody
    public Result update(@RequestBody TaskInfo taskInfo) {
        if(taskInfoService.update(taskInfo)>0)
            return new Result(100,"任务信息修改成功");
        else
            return new Result(100,"任务信息修改失败");
    }

    @DeleteMapping("delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Long id) {
        if(taskInfoService.delById(id)>0)
            return new Result(100,"任务信息删除成功");
        else
            return new Result(100,"任务信息删除失败");
    }

    @GetMapping("one/{id}")
    @ResponseBody
    public TaskInfo one(@PathVariable("id") Long id) {
        return taskInfoService.findById(id);
    }

    @GetMapping("list")
    @ResponseBody
    public List<TaskInfo> list(
            @RequestParam(value = "currentUserId", required = false) Long currentUserId,
            @RequestParam(value = "role", required = false) String role) {
        // 数据隔离：成员只能看到分配给自己和已创建的任务
        if (currentUserId != null && !"负责人".equals(role)) {
            return taskInfoService.findListByAssignee(currentUserId);
        }
        return taskInfoService.findList();
    }
}
