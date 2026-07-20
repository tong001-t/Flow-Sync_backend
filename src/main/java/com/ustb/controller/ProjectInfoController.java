package com.ustb.controller;

import com.ustb.common.Result;
import com.ustb.entity.ProjectInfo;
import com.ustb.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Controller表示当前类是一个controller类
//controller类可以被框架识别到，并通过URL地址进行访问
/*
@RequestMapping:将请求的URL地址映射到方法，可以相应Get，Post，Put，Delete
url:(uniform resuorce locator)可以在互联网中定位到一个资源
url传统格式:协议：//主机地址：端口号/资源路径？参数名=参数值&参数名=参数值
restful风格url地址：
协议：//主机地址：端口号/资源路径/参数1/参数2
 */
@Controller
//抽取映射的公共部分
@RequestMapping("/project/")
@CrossOrigin
public class ProjectInfoController {
    @Autowired
    private ProjectInfoService projectInfoService;

    //@RequestMapping(value = "add",method = RequestMethod.POST)
    //新增数据
    @PostMapping("add")
    @ResponseBody
    public Result add(@RequestBody ProjectInfo info){
        //@RequestBody：注解，自动将请求参数的json对象转化为java对象
        System.out.println("add()方法被执行了");
        System.out.println(info);
        if(projectInfoService.add(info)>0)
            return new Result(100,"项目信息添加成功");
        else
            return new Result(101,"项目信息添加失败");
    }
    //按Id修改数据
//    @RequestMapping("update")
    @PutMapping("update")
    @ResponseBody
    public Result update(@RequestBody ProjectInfo info){
        System.out.println("update()方法被执行了");
        System.out.println(info);

        if(projectInfoService.updateById(info)>0)
            return new Result(100,"项目信息修改成功");
        else
            return new Result(101,"项目信息修改失败");
    }
    //按Id删除数据
//    @RequestMapping("delete/{id}")
    @DeleteMapping("delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") long id){
        System.out.println("delete()方法被执行了");
        System.out.println("id="+id);
        if(projectInfoService.delById(id)>0)
            return new Result(100,"项目信息删除成功");
        else
            return new Result(101,"项目信息删除失败");
    }
    //按Id查询数据
    @ResponseBody//注解：表示将返回对象转为Json数据
//    @RequestMapping("one/{id}")
    @GetMapping("one/{id}")
    public ProjectInfo queryOne(@PathVariable("id") long id){
        System.out.println("one()方法被执行了");
        System.out.println("id="+id);
        return projectInfoService.findById(id);
    }
    /*按所有数据列表*/
    @ResponseBody
    //@RequestMapping("list")
    @GetMapping("list")
    public List<ProjectInfo> list(){
        System.out.println("list()方法被执行了");
        return projectInfoService.findList();
    }
}
