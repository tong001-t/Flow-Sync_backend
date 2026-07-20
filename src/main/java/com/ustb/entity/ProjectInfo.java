package com.ustb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
实体类，输出传递的载体
通常情况，一个实体类对应数据库中的一个表，实体类中的属性与数据库字段一一对应
实体类三要素
1.公共的类
2.私有成员字段
3.无参构造函数
4.提供get/set访问器
 */
/*
Lombok提供了一组注解
@Data：表示当前是一个实体类
@NoArgsConstructor提供一个无参构造器
@AllArgsConstructor提供一个带有所有参数的构造器
@ToString：重写类的toString方法
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private Long ownerId;
    private Date startDate;
    private Date endDate;
    private Date createTime;
}